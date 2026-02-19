package br.com.unicos.ms_compras.dto.produtocodigobarras;

import java.time.LocalDateTime;

public record ProdutoCodigoBarrasResponse(
        Long id,
        Long produtoId,
        String codigoBarras,
        Boolean principal,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
