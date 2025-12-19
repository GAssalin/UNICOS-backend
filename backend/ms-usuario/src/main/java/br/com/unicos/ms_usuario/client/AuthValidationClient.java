package br.com.unicos.ms_usuario.client;

import br.com.unicos.core.auth.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "ms-auth",
        contextId = "authValidationClient"
)
public interface AuthValidationClient {

    @PostMapping("/internal/auth/validate-token")
    TokenValidationResponse validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}
