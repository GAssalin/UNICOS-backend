package br.com.unicos.ms_produtos.dto.fornecedor_produto;

import java.math.BigDecimal;

/**
 * DTO utilizado para cadastrar ou atualizar o vínculo entre
 * um fornecedor e um produto.
 *
 * <p>
 * Contém apenas os dados enviados pelo cliente, sem informações
 * de auditoria ou entidades internas.
 * </p>
 */
public record FornecedorProdutoRequest(
        Long produtoId,
        Long fornecedorId,
        String codigoFornecedor,
        BigDecimal precoCusto,
        Integer prazoEntregaDias
) {}
