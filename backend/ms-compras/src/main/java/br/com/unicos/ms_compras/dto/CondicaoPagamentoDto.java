package br.com.unicos.ms_compras.dto;

/**
 * DTO de condição de pagamento.
 */
public record CondicaoPagamentoDto(
        Long id,
        String codigo,
        String nome,
        String descricao,
        Boolean parcelado
) { }
