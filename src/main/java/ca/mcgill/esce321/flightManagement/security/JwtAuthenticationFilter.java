package ca.mcgill.esce321.flightManagement.security;

import ca.mcgill.esce321.flightManagement.config.JwtConfigProperties;
import ca.mcgill.esce321.flightManagement.config.TokenType;
import ca.mcgill.esce321.flightManagement.service.token.CookieServiceImpl;
import ca.mcgill.esce321.flightManagement.service.token.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final CookieServiceImpl cookieService;
    private final UserDetailsService userDetailsService;
    private final JwtConfigProperties jwtConfigProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // Get token name from properties
            String tokenName = jwtConfigProperties.getTokenName();
            String jwtToken = cookieService.getTokenFromCookie(request, tokenName);

            // TODO: implement REFRESH TOKEN & BLACKLIST TOKEN

            // If a token is present, attempt authentication
            if (jwtToken != null) {
                authenticateUsingJwtToken(jwtToken, request);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException eje) {
            log.warn("Expired access token: {}", eje.getMessage());
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("JWT processing error: {}", ex.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    private void authenticateUsingJwtToken(String jwtToken, HttpServletRequest request) {
        final TokenType ACCESS_TOKEN = TokenType.ACCESS_TOKEN;

        try {
            String username = jwtService.extractUsername(jwtToken, ACCESS_TOKEN);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwtToken, userDetails, ACCESS_TOKEN)) {
                    setAuthenticationContext(userDetails, request);
                }
            }
        } catch (JwtException e) {
            log.info("Invalid access token encountered: {}", e.getMessage());
        }
        // If validation fails, null is returned
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}