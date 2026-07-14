package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioRoleIdsResponse;
import br.com.unicos.core.usuario.auth.dto.UsuarioRoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-permissao",
        contextId = "PermissaoClient"
)
public interface PermissaoClient {

    @PostMapping("/internal/permissao/check")
    boolean usuarioPossuiPermissao(@RequestParam("nomePermissao") String nomePermissao);

    @GetMapping("/internal/permissao/role/{id}")
    UsuarioRoleResponse buscarNomeRoleById(@PathVariable Long id);

}