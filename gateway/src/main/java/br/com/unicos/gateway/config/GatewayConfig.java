package br.com.unicos.gateway.config;

import br.com.unicos.gateway.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Rotas do gateway. Cada microserviço é exposto em {@code /{nome-do-servico}/**}, com o prefixo
 * removido antes do encaminhamento via Eureka ({@code lb://}).
 *
 * <p>
 * As rotas são avaliadas na ordem em que são declaradas: exceções públicas (sem JWT) precisam vir
 * antes da rota protegida do mesmo serviço. Caminhos {@code /internal/**} e {@code /actuator/**}
 * dos microserviços são bloqueados pelo {@code ProtecaoBordaFilter}.
 * </p>
 */
@RequiredArgsConstructor
@Configuration
public class GatewayConfig {

    /**
     * Microserviços cujas rotas exigem access token válido.
     */
    static final List<String> SERVICOS_PROTEGIDOS = List.of(
            "ms-permissao",
            "ms-pessoas",
            "ms-empresa",
            "ms-estoque",
            "ms-cliente",
            "ms-funcionario",
            "ms-produto"
    );

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes()

                // Login e renovação de token: sem JWT.
                .route("ms-autenticacao", r -> r
                        .path("/ms-autenticacao/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ms-autenticacao")
                )

                // Confirmação de e-mail: o usuário ainda não possui token.
                .route("ms-pessoas-confirmacao-email", r -> r
                        .path("/ms-pessoas/v1/verificacao-email/confirmar")
                        .and()
                        .method(HttpMethod.PATCH)
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ms-pessoas")
                );

        for (String servico : SERVICOS_PROTEGIDOS) {
            routes = routes.route(servico, r -> r
                    .path("/" + servico + "/**")
                    .filters(f -> f
                            .stripPrefix(1)
                            .filter(jwtAuthFilter)
                    )
                    .uri("lb://" + servico)
            );
        }

        return routes.build();
    }
}
