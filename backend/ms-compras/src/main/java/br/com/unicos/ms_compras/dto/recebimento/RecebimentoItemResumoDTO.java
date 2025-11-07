package br.com.unicos.ms_compras.dto.recebimento;

import java.math.BigDecimal;

/**
 * DTO resumido de um item de recebimento.
 *
 * <p>Utilizado em {@link RecebimentoCompraResponse} para exibir dados básicos dos itens recebidos.</p>
 */
public record RecebimentoItemResumoDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidadeRecebida,
        BigDecimal quantidadePrevista,
        BigDecimal quantidadeDevolvida,
        String observacao
) {}
