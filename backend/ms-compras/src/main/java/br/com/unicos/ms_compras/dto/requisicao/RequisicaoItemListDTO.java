package br.com.unicos.ms_compras.dto.requisicao;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem de itens de requisição de compra.
 *
 * <p>Fornece uma visão resumida para exibição em tabelas e consultas rápidas.</p>
 */
public record RequisicaoItemListDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidadeSolicitada,
        BigDecimal quantidadeAtendida,
        String observacao
) {}
