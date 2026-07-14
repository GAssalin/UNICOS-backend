package br.com.unicos.gateway.filter;

import br.com.unicos.core.auth.service.TokenCoreService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.unicos.core.base.error.ErrorUtils.respostaErro;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter implements GatewayFilter {

    private final TokenCoreService tokenCoreService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(">>> PASSOU PELO GATEWAY: {}", exchange.getRequest().getURI());

        String path = exchange.getRequest().getURI().getPath();

        // ==============================
        // ROTAS SEM JWT
        // ==============================
        if (path.contains("/v1/auth/login")) { return chain.filter(exchange); }

        try {
            DecodedJWT jwt = tokenCoreService.validarToken(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

            Long usuarioId = jwt.getClaim("usuarioId").asLong();
            Long tenantId = jwt.getClaim("tenantId").asLong();

            if (usuarioId == null || tenantId == null)
                throw new JWTVerificationException("usuarioId || tenantId não identificado.");

            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Usuario-Id", usuarioId.toString())
                    .header("X-Tenant-Id", tenantId.toString())
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return respostaErro(exchange, HttpStatus.UNAUTHORIZED, "Unauthorized", "Token ausente, inválido ou expirado.");
        }
    }

}
