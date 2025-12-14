package br.com.unicos.ms_produtos.dto.fornecedor_produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO utilizado para cadastrar ou atualizar o vínculo entre
 * um fornecedor e um produto.
 *
 * <p>
 * O contexto da empresa (tenant) é resolvido automaticamente
 * pelo backend a partir da autenticação do usuário.
 * </p>
 */
public record FornecedorProdutoRequest(

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O ID do fornecedor é obrigatório.")
        Long fornecedorId,

        String codigoFornecedor,

        @NotNull(message = "O preço de custo é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço de custo deve ser maior que zero.")
        BigDecimal precoCusto,

        Integer prazoEntregaDias
) {}
