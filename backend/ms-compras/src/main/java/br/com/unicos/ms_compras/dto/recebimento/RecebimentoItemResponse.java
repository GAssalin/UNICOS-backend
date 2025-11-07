package br.com.unicos.ms_compras.dto.recebimento;

import java.math.BigDecimal;

/**
 * DTO de resposta detalhada do item de recebimento de compra.
 *
 * <p>Inclui informações completas de quantidades, observações e vínculo com o recebimento.</p>
 */
public record RecebimentoItemResponse(

        Long id,
        Long produtoId,
        BigDecimal quantidadeRecebida,
        BigDecimal quantidadePrevista,
        BigDecimal quantidadeDevolvida,
        String observacao,
        Long recebimentoCompraId
) {}
