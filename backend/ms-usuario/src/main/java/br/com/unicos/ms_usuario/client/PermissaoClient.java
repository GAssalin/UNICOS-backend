package br.com.unicos.ms_usuario.client;

import br.com.unicos.ms_usuario.dto.permissao.RoleResumoResponse;
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
    boolean usuarioPossuiPermissao(@RequestParam String nomePermissao);

    @GetMapping("/internal/roles/{id}")
    RoleResumoResponse buscarRolePorId(@PathVariable Long id);
}
