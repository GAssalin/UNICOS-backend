package br.com.unicos.ms_auth.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-usuario",
        contextId = "UsuarioClient"
)
public interface UsuarioClient {

    @GetMapping("/internal/auth/by-email")
    UsuarioAuthResponse buscarPorEmail(@RequestParam String email);

}