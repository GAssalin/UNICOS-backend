package br.com.unicos.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ProtecaoBordaFilterTest {

    private final ProtecaoBordaFilter filter = new ProtecaoBordaFilter();

    @Test
    void deveBloquearEndpointsInternosEActuatorDosServicos() {
        assertThat(bloqueado("/ms-pessoas/internal/auth/by-email")).isTrue();
        assertThat(bloqueado("/ms-pessoas/INTERNAL/auth/by-email")).isTrue();
        assertThat(bloqueado("/ms-pessoas/%69nternal/auth/by-email")).isTrue();
        assertThat(bloqueado("/ms-pessoas/internal;x=1/auth")).isTrue();
        assertThat(bloqueado("/ms-permissao/internal")).isTrue();
        assertThat(bloqueado("/ms-produto/actuator/health")).isTrue();
        assertThat(bloqueado("/ms-produto/v1/../internal/x")).isTrue();
    }

    @Test
    void deveBloquearVariacoesComBarrasDuplicadasOuCodificadas() {
        assertThat(bloqueado("/ms-pessoas//internal/auth/by-email")).isTrue();
        assertThat(bloqueado("//ms-pessoas/internal/auth/by-email")).isTrue();
        assertThat(bloqueado("/ms-pessoas///actuator/env")).isTrue();
        assertThat(bloqueado("/ms-pessoas/internal%2Fauth/by-email")).isTrue();
        assertThat(bloqueado("/ms-pessoas/v1%5Cpessoas")).isTrue();
    }

    @Test
    void devePermitirRotasDeNegocio() {
        assertThat(bloqueado("/ms-pessoas/v1/pessoas")).isFalse();
        assertThat(bloqueado("/ms-autenticacao/v1/autenticacao/login")).isFalse();
        assertThat(bloqueado("/ms-produto/v1/produtos/internal")).isFalse();
        assertThat(bloqueado("/ms-pessoas/swagger-ui/index.html")).isFalse();
    }

    @Test
    void deveResponder404SemEncaminharCaminhoBloqueado() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/ms-pessoas/internal/auth/by-email").build());
        AtomicReference<ServerWebExchange> encaminhado = new AtomicReference<>();

        filter.filter(exchange, chainQueRegistra(encaminhado)).block();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(encaminhado.get()).isNull();
    }

    @Test
    void deveRemoverHeadersRestritosEnviadosPeloCliente() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/ms-pessoas/v1/pessoas")
                        .header("X-Internal-Token", "forjado")
                        .header("X-Usuario-Id", "1")
                        .header("X-Tenant-Id", "1")
                        .header("Authorization", "Bearer abc")
                        .build());
        AtomicReference<ServerWebExchange> encaminhado = new AtomicReference<>();

        filter.filter(exchange, chainQueRegistra(encaminhado)).block();

        assertThat(encaminhado.get()).isNotNull();
        var headers = encaminhado.get().getRequest().getHeaders();
        ProtecaoBordaFilter.HEADERS_RESTRITOS.forEach(header -> assertThat(headers.containsKey(header)).isFalse());
        assertThat(headers.getFirst("Authorization")).isEqualTo("Bearer abc");
    }

    private static boolean bloqueado(String path) {
        return ProtecaoBordaFilter.isCaminhoBloqueado(PathContainer.parsePath(path));
    }

    private static GatewayFilterChain chainQueRegistra(AtomicReference<ServerWebExchange> encaminhado) {
        return exchange -> {
            encaminhado.set(exchange);
            return Mono.empty();
        };
    }
}
