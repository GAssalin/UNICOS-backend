package br.com.unicos.ms_produtos.dto.produto_unidade;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO utilizado para criação ou atualização de unidades de produto.
 *
 * <p>
 * Representa a associação entre um produto e uma unidade de medida,
 * incluindo quantidade padrão e fator de conversão.
 * </p>
 */
public record ProdutoUnidadeRequest(
        @NotNull(message = "A unidade de medida é obrigatória.")
        Long unidadeMedidaId,
        @NotNull(message = "A quantidade padrão é obrigatória.")
        @Positive(message = "A quantidade padrão deve ser maior que zero.")
        Double quantidadePadrao,
        @Positive(message = "O fator de conversão deve ser maior que zero.")
        Double fatorConversao
) {}
