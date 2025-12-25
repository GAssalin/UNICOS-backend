package br.com.unicos.ms_usuario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-auth",
        contextId = "authPermissionClient"
)
public interface AuthPermissionClient {

    @PostMapping("/internal/auth/permissions/check")
    boolean usuarioPossuiPermissao(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestParam String nomePermissao);
}