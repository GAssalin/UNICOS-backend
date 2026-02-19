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
                // ROTA: MS-AUTENTICACAO (sem JWT)
                // ===============================
                .route("ms-autenticacao", r -> r
                        .path("/ms-autenticacao/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-autenticacao")
                )

                // ===============================
                // ROTA: MS-PERMISSAO (COM JWT)
                // ===============================
                .route("ms-permissao", r -> r
                        .path("/ms-permissao/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-permissao")
                )

                // ===============================
                // ROTA: MS-USUARIO (COM JWT)
                // ===============================
                .route("ms-usuario", r -> r
                        .path("/ms-usuario/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-usuario")
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

                // ===============================
                // ROTA: MS-EMPRESA (COM JWT)
                // ===============================
                .route("ms-empresa", r -> r
                        .path("/ms-empresa/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-empresa")
                )

                // ===============================
                // ROTA: MS-FILIAL (COM JWT)
                // ===============================
                .route("ms-filial", r -> r
                        .path("/ms-filial/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-filial")
                )

                // ===============================
                // ROTA: MS-DEPARTAMENTO (COM JWT)
                // ===============================
                .route("ms-departamento", r -> r
                        .path("/ms-departamento/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-departamento")
                )

                // ===============================
                // ROTA: MS-ESTOQUE (COM JWT)
                // ===============================
                .route("ms-estoque", r -> r
                        .path("/ms-estoque/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthFilter)
                        )
                        .uri("lb://ms-estoque")
                )

                .build();
    }
}