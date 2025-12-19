package br.com.unicos.ms_auth.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "ms-usuario",
        contextId = "authUsuarioClient"
)
public interface AuthUsuarioClient {

    @GetMapping("/internal/auth/by-email")
    UsuarioAuthResponse buscarPorEmail(@RequestParam String email);

}