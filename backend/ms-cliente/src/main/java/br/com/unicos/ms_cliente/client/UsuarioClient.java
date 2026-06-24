package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioRoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-usuario",
        contextId = "UsuarioClient"
)
public interface UsuarioClient {

    @GetMapping("/internal/usuarios/{id}/role")
    UsuarioRoleResponse buscarRoleDoUsuario(@PathVariable Long id);
}
