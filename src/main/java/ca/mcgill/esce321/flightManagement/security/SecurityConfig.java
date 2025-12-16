package ca.mcgill.esce321.flightManagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final JwtAuthenticationFilter jwAuthenFilter;
    private final HostCheckFilter hostCheckFilter;

    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())//enforce https
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/bookings/**").permitAll()
                        .requestMatchers("/api/employees/**").permitAll()
                        .requestMatchers("/api/flight-attendants/**").permitAll()
                        .requestMatchers("/api/flights/**").permitAll()
                        .requestMatchers("/api/managers/**").permitAll()
                        .requestMatchers("/api/owners/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/persons/login", "/api/persons/signup").permitAll()
                        // TODO: add profile update & password update
                        .requestMatchers(HttpMethod.PUT, "/api/customers/reset_password").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(hostCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwAuthenFilter, UsernamePasswordAuthenticationFilter.class)

                // bru, csrf method deprecated
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}