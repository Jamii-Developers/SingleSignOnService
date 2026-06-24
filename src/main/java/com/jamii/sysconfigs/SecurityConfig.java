package com.jamii.sysconfigs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Central security configuration for the JamiiX platform.
 *
 * <p>
 * This configuration is responsible for:
 * </p>
 * <ul>
 *     <li>Configuring HTTP security rules.</li>
 *     <li>Enabling method-level authorization via {@code @PreAuthorize}.</li>
 *     <li>Defining Cross-Origin Resource Sharing (CORS) policies.</li>
 *     <li>Enforcing stateless request processing.</li>
 *     <li>Exposing public endpoints and API documentation.</li>
 * </ul>
 *
 * <p>
 * The application follows a deny-by-default security model where all requests
 * are blocked unless explicitly permitted.
 * </p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig
{
    /**
     * Endpoints that can be accessed without authentication.
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/health",
            "/api/monitor-health",
            "/api/public/**",
            "/api/user/**"
    };

    /**
     * Endpoints used for OpenAPI and Swagger documentation.
     */
    private static final String[] DOCUMENTATION_ENDPOINTS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    };

    /**
     * Comma-separated list of allowed CORS origins.
     *
     * <p>Configured via application properties:</p>
     *
     * <pre>
     * app.cors.allowed-origins=http://localhost:3000,https://jamiix.netlify.app
     * </pre>
     */
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    /**
     * Configures the application's HTTP security rules.
     *
     * <p>
     * Security characteristics:
     * </p>
     * <ul>
     *     <li>CORS enabled.</li>
     *     <li>CSRF disabled because the API is stateless.</li>
     *     <li>No HTTP session creation.</li>
     *     <li>Public and documentation endpoints are accessible anonymously.</li>
     *     <li>All other requests are denied unless explicitly configured.</li>
     * </ul>
     *
     * @param http Spring Security HTTP configuration object
     * @return configured security filter chain
     * @throws Exception if the security configuration cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception
    {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // API documentation
                        .requestMatchers(DOCUMENTATION_ENDPOINTS).permitAll()

                        // Explicitly deny all other requests
                        .anyRequest().denyAll());

        return http.build();
    }

    /**
     * Creates the application's Cross-Origin Resource Sharing (CORS)
     * configuration.
     *
     * <p>
     * Allowed origins are sourced from application configuration to support
     * environment-specific deployments such as local development, UAT,
     * and production.
     * </p>
     *
     * <p>
     * Credentials are enabled to support authenticated browser requests.
     * </p>
     *
     * @return configured CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .toList()
        );

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}