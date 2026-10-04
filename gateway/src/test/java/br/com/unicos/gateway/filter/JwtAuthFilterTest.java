package br.com.unicos.gateway.filter;

import br.com.unicos.core.auth.service.JwtClaims;
import br.com.unicos.core.auth.service.TokenCoreService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthFilterTest {

    private static final String SEGREDO = "segredo-de-teste-com-mais-de-32-caracteres";
    private static final String ISSUER = "unicos-teste";

    private final JwtAuthFilter filter = new JwtAuthFilter(new TokenCoreService(SEGREDO, ISSUER));

    @Test
    void deveEncaminharRequisicaoComTokenValido() {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim(JwtClaims.TIPO, JwtClaims.TIPO_ACCESS)
                .withClaim(JwtClaims.USUARIO_ID, 1L)
                .withClaim(JwtClaims.TENANT_ID, 1L)
                .withExpiresAt(Instant.now().plusSeconds(60))
                .sign(Algorithm.HMAC256(SEGREDO));

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/ms-pessoas/v1/pessoas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build());
        AtomicBoolean encaminhado = new AtomicBoolean();

        filter.filter(exchange, chain(encaminhado)).block();

        assertThat(encaminhado).isTrue();
    }

    @Test
    void deveResponder401ComProblemDetailQuandoTokenAusente() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/ms-pessoas/v1/pessoas").build());
        AtomicBoolean encaminhado = new AtomicBoolean();

        filter.filter(exchange, chain(encaminhado)).block();

        assertThat(encaminhado).isFalse();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exchange.getResponse().getHeaders().getContentType()).hasToString("application/problem+json");
        assertThat(exchange.getResponse().getBodyAsString().block()).contains("\"status\":401");
    }

    private static GatewayFilterChain chain(AtomicBoolean encaminhado) {
        return exchange -> {
            encaminhado.set(true);
            return Mono.empty();
        };
    }
}
