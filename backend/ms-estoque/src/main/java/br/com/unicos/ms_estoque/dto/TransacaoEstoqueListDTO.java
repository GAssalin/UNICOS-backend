package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.TipoTransacao;
import java.time.LocalDateTime;

/**
 * DTO resumido para listagens de transações de estoque.
 */
public record TransacaoEstoqueListDTO(
        Long id,
        TipoTransacao tipo,
        LocalDateTime data
) {}
