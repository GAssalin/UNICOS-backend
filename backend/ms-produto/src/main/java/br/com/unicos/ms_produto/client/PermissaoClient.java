package br.com.unicos.ms_produto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-permissao")
public interface PermissaoClient {

    @PostMapping("/internal/permissao/check")
    boolean usuarioPossuiPermissao(
            @RequestParam("nomePermissao") String nomePermissao,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

}