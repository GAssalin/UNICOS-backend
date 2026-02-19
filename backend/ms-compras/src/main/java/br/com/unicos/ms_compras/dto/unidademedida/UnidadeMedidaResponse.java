package br.com.unicos.ms_compras.dto.unidademedida;

import java.time.LocalDateTime;

public record UnidadeMedidaResponse(
        Long id,
        String codigo,
        String descricao,
        Boolean fracionavel,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
