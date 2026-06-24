package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioRoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-permissao")
public interface PermissaoClient {

    @PostMapping("/internal/permissao/check")
    boolean usuarioPossuiPermissao(
            @RequestParam("nomePermissao") String nomePermissao,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @GetMapping("/internal/permissao/role/{id}")
    UsuarioRoleResponse buscarNomeRoleById(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);
}