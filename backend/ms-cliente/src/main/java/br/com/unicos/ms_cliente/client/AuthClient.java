package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.auth.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-autenticacao")
public interface AuthClient {

    @PostMapping("/internal/autenticacao/validate-token")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String authorizationHeader);
}