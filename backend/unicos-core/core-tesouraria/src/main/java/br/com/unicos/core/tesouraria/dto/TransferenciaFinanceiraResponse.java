package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoTransferencia;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta contendo as informações completas de uma transferência financeira.
 */
public record TransferenciaFinanceiraResponse(
        Long id,
        Long contaOrigemId,
        Long contaDestinoId,
        TipoTransferencia tipoTransferencia,
        MeioPagamento meioPagamento,
        BigDecimal valor,
        LocalDate dataTransferencia,
        String observacao,
        String usuarioResponsavel
) { }
