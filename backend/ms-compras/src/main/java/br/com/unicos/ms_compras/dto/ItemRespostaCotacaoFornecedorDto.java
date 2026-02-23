package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;

/**
 * DTO de item da resposta de cotação do fornecedor.
 */
public record ItemRespostaCotacaoFornecedorDto(
        Long id,
        Long respostaCotacaoFornecedorId,
        Long itemCotacaoId,
        BigDecimal precoUnitario,
        BigDecimal descontoItem,
        BigDecimal totalItem,
        Integer prazoEntregaDias,
        String observacao
) { }
