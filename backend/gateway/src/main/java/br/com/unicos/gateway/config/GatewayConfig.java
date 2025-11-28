package br.com.unicos.gateway.config;

import br.com.unicos.gateway.security.JwtAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

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
                                .filter(new JwtAuthFilter()) // FILTRO JWT AQUI!
                        )
                        .uri("lb://ms-produtos")
                )

                .build();
    }
}