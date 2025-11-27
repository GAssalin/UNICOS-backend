package br.com.unicos.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

public class JwtAuthFilter implements GatewayFilter {

    private static final String SECRET = "1234"; // mesma chave do MS-AUTH

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders headers = exchange.getRequest().getHeaders();

        if (!headers.containsKey("Authorization")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = headers.getFirst("Authorization").replace("Bearer ", "");

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWT.require(algorithm)
                    //.withIssuer("se tiver issuer, declare aqui")
                    .build()
                    .verify(token);

            return chain.filter(exchange);

        } catch (JWTVerificationException e) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
    }
}
