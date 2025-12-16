package ca.mcgill.esce321.flightManagement.service.token;

import ca.mcgill.esce321.flightManagement.config.JwtConfigProperties;
import ca.mcgill.esce321.flightManagement.config.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl {
    private final JwtConfigProperties jwtConfigProperties;
    private final Key signInKey;

    /**
     * Initializes the service with JWT and RT (Refresh Token) configuration properties.
     *
     * @param jwtConfigProperties Configuration properties for JWT.
     */
    public JwtServiceImpl(JwtConfigProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
        this.signInKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getSecret()));
    }

    /**
     * Extracts the username from a token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractUsername(token, TokenType.ACCESS_TOKEN);
    }

    /**
     * Extracts the username from a token of a specified type.
     *
     * @param token     The JWT token.
     * @param tokenType The type of the token (ACCESS or REFRESH).
     * @return The username extracted from the token.
     */
    public String extractUsername(String token, TokenType tokenType) {
        return extractClaim(token, Claims::getSubject, tokenType);
    }

    /**
     * Extracts a specific claim from a token.
     *
     * @param token         The JWT token.
     * @param claimsResolver A function to extract the desired claim.
     * @param tokenType     The type of the token (ACCESS or REFRESH).
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, TokenType tokenType) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a token for a user with specified token type.
     *
     * @param userDetails UserDetails for the subject of the token.
     * @param tokenType   The type of the token (ACCESS or REFRESH).
     * @return A new JWT token.
     */
    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        return generateToken(new HashMap<>(), userDetails, tokenType);
    }

    /**
     * Generates a token for a user with specified token type.
     *
     * @param claims      Claims to include in the token.
     * @param userDetails UserDetails for the subject of the token.
     * @param tokenType   The type of the token (ACCESS or REFRESH).
     * @return A new JWT token.
     */
    public String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails,
            TokenType tokenType
    ) {
        // Assuming 'expiration' is the field name in JwtConfigProperties
        long expirationTimeLong = jwtConfigProperties.getExpiration();

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        return Jwts.builder()

                // TODO: so many deprecated methods....
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)

                // TODO: implement refresh token logic later
                .signWith(signInKey)
                .compact();
    }

    /**
     * Validates a token by checking its expiration and comparing its subject with UserDetails.
     *
     * @param token       The JWT token to validate.
     * @param userDetails UserDetails to compare with token subject.
     * @param tokenType   The type of the token (ACCESS or REFRESH).
     * @return true if the token is valid; false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        final String username = extractUsername(token,tokenType);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(token, tokenType);
    }

    /**
     * Extracts the expiration date from a token.
     *
     * @param token     The JWT token.
     * @param tokenType The type of the token (ACCESS or REFRESH).
     * @return The expiration date extracted from the token.
     */
    public Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, Claims::getExpiration, tokenType);
    }


    /**
     * Checks if a token has expired.
     *
     * @param token     The JWT token to check.
     * @param tokenType The type of the token (ACCESS or REFRESH).
     * @return true if the token has expired; false otherwise.
     */
    public boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token,tokenType).before(new Date());
    }

    // Helper method to extract all claims from a token
    private Claims extractAllClaims(String token, TokenType tokenType) throws JwtException{
        try {
            return Jwts.parser()

                    // TODO: implement refresh token logic later
                    .setSigningKey(signInKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Token expired: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.error("Could not parse token: {}", e.getMessage());
            throw e;
        }
    }
}
