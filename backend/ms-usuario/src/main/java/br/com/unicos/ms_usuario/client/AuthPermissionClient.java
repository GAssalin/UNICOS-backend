package br.com.unicos.ms_usuario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-auth",
        contextId = "AuthPermissionClient"
)
public interface AuthPermissionClient {

    @PostMapping("/internal/auth/permissions/check")
    boolean usuarioPossuiPermissao(@RequestParam Long usuarioId, @RequestParam Long empresaId, @RequestParam String permissao);
}