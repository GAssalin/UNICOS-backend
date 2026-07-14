package br.com.unicos.ms_permissao.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioRoleIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-usuario",
        contextId = "UsuarioClient"
)
public interface UsuarioClient {

    @GetMapping("/internal/usuarios/{id}/role")
    UsuarioRoleIdsResponse buscarRoleDoUsuario(@PathVariable Long id);
}
