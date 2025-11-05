package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.StatusInventario;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO utilizado para abertura e atualização de inventários de estoque.
 */
public record InventarioEstoqueRequest(
        @NotNull Long estoqueLocalId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusInventario status
) {}
