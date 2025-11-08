package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoTransferencia;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de uma transferência financeira.
 */
public record TransferenciaFinanceiraRequest(
        @NotNull Long contaOrigemId,
        @NotNull Long contaDestinoId,
        @NotNull TipoTransferencia tipoTransferencia,
        @NotNull MeioPagamento meioPagamento,
        @NotNull BigDecimal valor,
        @NotNull LocalDate dataTransferencia,
        String observacao,
        String usuarioResponsavel
) { }
