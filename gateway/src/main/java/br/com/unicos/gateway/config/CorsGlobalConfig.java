package br.com.unicos.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

/**
 * CORS centralizado no gateway.
 *
 * <p>
 * As origens permitidas vêm de {@code unicos.cors.allowed-origin-patterns} (variável
 * {@code CORS_ALLOWED_ORIGINS}, separada por vírgulas). Em produção, informe explicitamente
 * o domínio do frontend em vez do padrão {@code *}.
 * </p>
 */
@Configuration
public class CorsGlobalConfig {

    @Bean
    public CorsWebFilter corsWebFilter(
            @Value("${unicos.cors.allowed-origin-patterns:*}") List<String> allowedOriginPatterns
    ) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(allowedOriginPatterns);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        // A API usa token Bearer, não cookies.
        config.setAllowCredentials(false);
        config.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
