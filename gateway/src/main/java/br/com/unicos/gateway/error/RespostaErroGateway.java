package br.com.unicos.gateway.error;

import br.com.unicos.core.base.error.ProblemaJson;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Escreve respostas de erro do gateway no mesmo formato Problem Details usado pelos microserviços.
 */
public final class RespostaErroGateway {

    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType(ProblemaJson.CONTENT_TYPE);

    private RespostaErroGateway() {
    }

    public static Mono<Void> escrever(ServerWebExchange exchange, HttpStatus status, String detalhe) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted())
            return Mono.empty();

        response.setStatusCode(status);
        response.getHeaders().setContentType(PROBLEM_JSON);

        byte[] bytes = ProblemaJson.serializar(
                status.value(),
                status.getReasonPhrase(),
                detalhe,
                exchange.getRequest().getPath().value()
        ).getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
