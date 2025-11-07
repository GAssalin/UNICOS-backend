package br.com.unicos.ms_compras.dto.recebimento;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem de itens de recebimento de compra.
 *
 * <p>Fornece uma visão resumida para exibição em tabelas e consultas rápidas.</p>
 */
public record RecebimentoItemListDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidadeRecebida,
        BigDecimal quantidadePrevista,
        BigDecimal quantidadeDevolvida,
        String observacao
) {}
