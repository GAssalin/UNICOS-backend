package br.com.unicos.ms_compras.dto.categoria;

import java.time.LocalDateTime;

public record CategoriaProdutoResponse(
        Long id,
        String nome,
        String descricao,
        Long categoriaPaiId,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
