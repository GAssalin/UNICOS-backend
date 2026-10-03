package br.com.unicos.gateway.filter;

import br.com.unicos.gateway.error.RespostaErroGateway;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Proteções aplicadas a todo tráfego externo, antes de qualquer outro filtro do gateway.
 *
 * <ul>
 *     <li>Bloqueia {@code /{servico}/internal/**} (comunicação entre serviços) e
 *     {@code /{servico}/actuator/**} (operação/monitoramento) dos microserviços.</li>
 *     <li>Remove headers que apenas a própria plataforma pode definir (token interno e
 *     identidade), impedindo que o cliente os forje.</li>
 * </ul>
 */
@Component
public class ProtecaoBordaFilter implements GlobalFilter, Ordered {

    static final List<String> HEADERS_RESTRITOS = List.of("X-Internal-Token", "X-Usuario-Id", "X-Tenant-Id");

    private static final Set<String> SEGMENTOS_BLOQUEADOS = Set.of("internal", "actuator");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isCaminhoBloqueado(exchange.getRequest().getPath().pathWithinApplication()))
            return RespostaErroGateway.escrever(exchange, HttpStatus.NOT_FOUND, "Recurso não encontrado.");

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(headers -> HEADERS_RESTRITOS.forEach(headers::remove))
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * Considera os segmentos já decodificados ({@code %69nternal} equivale a {@code internal})
     * e bloqueia caminhos não normalizados ({@code ..}).
     */
    static boolean isCaminhoBloqueado(PathContainer path) {
        int indiceSegmento = 0;

        for (PathContainer.Element element : path.elements()) {
            if (!(element instanceof PathContainer.PathSegment segmento))
                continue;

            String valor = segmento.valueToMatch().toLowerCase(Locale.ROOT);

            if (valor.equals("..") || valor.equals("."))
                return true;

            if (indiceSegmento == 1 && SEGMENTOS_BLOQUEADOS.contains(valor))
                return true;

            indiceSegmento++;
        }

        return false;
    }
}
