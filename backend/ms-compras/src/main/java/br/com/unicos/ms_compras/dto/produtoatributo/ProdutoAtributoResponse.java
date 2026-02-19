package br.com.unicos.ms_compras.dto.produtoatributo;

import java.time.LocalDateTime;

public record ProdutoAtributoResponse(
        Long id,
        String nome,
        String descricao,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
