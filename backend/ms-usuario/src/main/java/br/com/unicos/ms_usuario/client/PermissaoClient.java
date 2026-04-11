package br.com.unicos.ms_usuario.client;

import br.com.unicos.ms_usuario.dto.permissao.RoleResumoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-permissao")
public interface PermissaoClient {

    @GetMapping("/internal/roles/{id}")
    RoleResumoResponse buscarRolePorId(
            @PathVariable("id") Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @PostMapping("/internal/permissao/check")
    boolean usuarioPossuiPermissao(
            @RequestParam("nomePermissao") String nomePermissao,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

}