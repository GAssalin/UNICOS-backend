package br.com.unicos.ms_usuario.client;

import br.com.unicos.ms_usuario.dto.permissao.RoleResumoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-permissao")
public interface PermissaoClient {

    @GetMapping("/internal/roles/{id}")
    RoleResumoResponse buscarRolePorId(@PathVariable("id") Long id);

    @PostMapping("/internal/permissao/check")
    boolean usuarioPossuiPermissao(@RequestParam("nomePermissao") String nomePermissao);
}