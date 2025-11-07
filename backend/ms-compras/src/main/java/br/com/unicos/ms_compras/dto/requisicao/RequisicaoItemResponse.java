package br.com.unicos.ms_compras.dto.requisicao;

import java.math.BigDecimal;

/**
 * DTO de resposta detalhada de um item de requisição de compra.
 *
 * <p>Inclui informações de produto, quantidades e vínculo com a requisição.</p>
 */
public record RequisicaoItemResponse(

        Long id,
        Long produtoId,
        BigDecimal quantidadeSolicitada,
        BigDecimal quantidadeAtendida,
        String observacao,
        Long requisicaoCompraId
) {}
