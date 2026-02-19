package br.com.unicos.ms_compras.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-permissao",
        contextId = "PermissaoClient"
)
public interface PermissaoClient {

    @PostMapping("/internal/permissao/check")
    boolean usuarioPossuiPermissao(@RequestParam String nomePermissao);

}