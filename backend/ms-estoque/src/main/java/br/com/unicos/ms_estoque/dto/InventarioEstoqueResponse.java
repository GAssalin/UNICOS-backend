package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.StatusInventario;
import java.time.LocalDateTime;

/**
 * DTO de retorno com os dados de um inventário de estoque.
 */
public record InventarioEstoqueResponse(
        Long id,
        Long estoqueLocalId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusInventario status
) {}
