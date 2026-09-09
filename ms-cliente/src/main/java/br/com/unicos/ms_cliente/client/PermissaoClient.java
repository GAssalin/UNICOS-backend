package br.com.unicos.ms_cliente.client;

import br.com.unicos.ms_cliente.dto.internal.RoleResumoResponse;
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

    @GetMapping("/internal/roles/{id}")
    RoleResumoResponse buscarNomeRoleById(@PathVariable Long id);

}