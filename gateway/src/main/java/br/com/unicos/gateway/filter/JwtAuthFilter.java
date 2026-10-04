package br.com.unicos.gateway.filter;

import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.gateway.error.RespostaErroGateway;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Rejeita na borda requisições sem access token válido.
 *
 * <p>
 * O header {@code Authorization} segue para o microserviço, que valida novamente o token e
 * dele extrai usuário e empresa; o gateway não injeta headers de identidade.
 * </p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter implements GatewayFilter {

    private final TokenCoreService tokenCoreService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            tokenCoreService.validarToken(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        } catch (JWTVerificationException e) {
            log.debug("Requisição rejeitada no gateway [{}]: {}", exchange.getRequest().getPath(), e.getMessage());
            return RespostaErroGateway.escrever(exchange, HttpStatus.UNAUTHORIZED, "Token ausente, inválido ou expirado.");
        }

        return chain.filter(exchange);
    }
}
