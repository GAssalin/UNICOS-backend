package br.com.unicos.ms_permissao.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "ms-permissao",
        contextId = "PermissaoClient"
)
public interface PermissaoClient {

    @GetMapping("/internal/permissao/find-roles-by-user-email")
    Set<String> findRolesByUsuario(@RequestParam String email);

}