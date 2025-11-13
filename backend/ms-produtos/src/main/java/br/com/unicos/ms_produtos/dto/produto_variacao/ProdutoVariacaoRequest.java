package br.com.unicos.ms_produtos.dto.produto_variacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de variações de produto.
 *
 * <p>
 * Representa uma unidade específica do produto principal,
 * podendo incluir SKU exclusivo, preço próprio e atributos como cor,
 * tamanho ou material.
 * </p>
 */
public record ProdutoVariacaoRequest(
        @NotBlank(message = "O nome da variação é obrigatório.")
        @Size(max = 150)
        String nome,
        @NotBlank(message = "O SKU da variação é obrigatório.")
        @Size(max = 50)
        String sku,
        @DecimalMin(value = "0.0", inclusive = false,
                message = "O preço deve ser maior que zero.")
        BigDecimal preco,
        @Size(max = 13)
        String codigoBarras,
        @Size(max = 50)
        String cor,
        @Size(max = 50)
        String tamanho,
        @Size(max = 100)
        String material
) {}
