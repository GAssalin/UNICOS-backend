package br.com.unicos.ms_compras.dto.requisicao;

import java.math.BigDecimal;

/**
 * DTO resumido dos itens de uma requisição de compra.
 *
 * <p>Utilizado dentro de {@link RequisicaoCompraResponse} para exibir dados básicos.</p>
 */
public record RequisicaoItemResumoDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidadeSolicitada,
        BigDecimal quantidadeAtendida,
        String observacao
) {}
