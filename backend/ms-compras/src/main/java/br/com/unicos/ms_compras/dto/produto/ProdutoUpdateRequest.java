package br.com.unicos.ms_compras.dto.produto;

import br.com.unicos.ms_compras.enums.TipoProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de requisição para atualização de {@code Produto}.
 */
public record ProdutoUpdateRequest(

        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,
        String descricao,
        @NotNull(message = "O tipo do produto é obrigatório.")
        TipoProduto tipoProduto,
        @NotNull(message = "A unidade de medida é obrigatória.")
        Long unidadeMedidaId,
        Long categoriaId,
        Long marcaId,
        String codigoBarras,
        BigDecimal precoBase,
        BigDecimal peso,
        BigDecimal volume

) {}
