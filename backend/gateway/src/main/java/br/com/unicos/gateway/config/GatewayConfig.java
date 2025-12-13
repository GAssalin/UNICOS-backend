package br.com.unicos.gateway.config;

import br.com.unicos.gateway.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class GatewayConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("docs-page", r -> r
                        .path("/docs")
                        .uri("no://op") // rota interna, sem backend
                )

                // ===============================
                // ROTA: MS-AUTH (sem JWT)
                // ===============================
                .route("ms-auth", r -> r
                        .path("/ms-auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ms-auth")
                )

                // ===============================
                // ROTA: MS-PRODUTOS (COM JWT)
                // ===============================
                .route("ms-produtos", r -> r
                        .path("/ms-produtos/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-produtos")
                )

                // ===============================
                // ROTA: MS-PESSOAS (COM JWT)
                // ===============================
                .route("ms-pessoas", r -> r
                        .path("/ms-pessoas/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-pessoas")
                )

                .build();
    }
}