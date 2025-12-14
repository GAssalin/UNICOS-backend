package br.com.unicos.ms_produtos.dto.produto_variacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de variações de produto.
 *
 * <p>
 * Representa uma unidade específica do produto principal.
 * O produto e a empresa (tenant) são resolvidos automaticamente
 * pelo backend a partir do contexto da requisição.
 * </p>
 */
public record ProdutoVariacaoRequest(

        @NotBlank(message = "O nome da variação é obrigatório.")
        @Size(max = 150, message = "O nome da variação deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O SKU da variação é obrigatório.")
        @Size(max = 50, message = "O SKU da variação deve ter no máximo 50 caracteres.")
        String sku,

        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        BigDecimal preco,

        @Size(max = 13, message = "O código de barras deve ter no máximo 13 caracteres.")
        String codigoBarras,

        @Size(max = 50, message = "A cor deve ter no máximo 50 caracteres.")
        String cor,

        @Size(max = 50, message = "O tamanho deve ter no máximo 50 caracteres.")
        String tamanho,

        @Size(max = 100, message = "O material deve ter no máximo 100 caracteres.")
        String material
) {}
