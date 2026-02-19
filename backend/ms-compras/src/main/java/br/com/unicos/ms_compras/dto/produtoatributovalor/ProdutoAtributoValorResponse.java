package br.com.unicos.ms_compras.dto.produtoatributovalor;

import java.time.LocalDateTime;

public record ProdutoAtributoValorResponse(
        Long id,
        Long produtoId,
        Long atributoId,
        String valor,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
