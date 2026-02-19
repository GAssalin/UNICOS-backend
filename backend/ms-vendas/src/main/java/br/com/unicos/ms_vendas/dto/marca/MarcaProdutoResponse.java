package br.com.unicos.ms_vendas.dto.marca;

import java.time.LocalDateTime;

public record MarcaProdutoResponse(
        Long id,
        String nome,
        String descricao,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
