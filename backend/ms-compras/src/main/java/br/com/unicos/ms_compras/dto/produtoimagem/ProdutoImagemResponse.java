package br.com.unicos.ms_compras.dto.produtoimagem;

import java.time.LocalDateTime;

public record ProdutoImagemResponse(
        Long id,
        Long produtoId,
        String url,
        String altTexto,
        Boolean principal,
        Integer ordem,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
