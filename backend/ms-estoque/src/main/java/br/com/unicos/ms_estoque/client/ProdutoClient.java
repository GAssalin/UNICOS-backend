package br.com.unicos.ms_estoque.client;

import br.com.unicos.core.produto.dto.ProdutoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "ms-produto",
        contextId = "ProductClient"
)
public interface ProdutoClient {

    @GetMapping("/{id}")
    ProdutoResponseDto buscarPorId(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

}