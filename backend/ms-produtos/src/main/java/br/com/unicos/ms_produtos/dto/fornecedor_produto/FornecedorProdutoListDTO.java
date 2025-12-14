package br.com.unicos.ms_produtos.dto.fornecedor_produto;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagens de vínculos entre fornecedores e produtos.
 *
 * <p>
 * Fornece uma visão resumida do relacionamento fornecedor–produto,
 * contendo apenas informações essenciais para exibição em listas.
 * O contexto da empresa (tenant) é resolvido automaticamente no backend.
 * </p>
 */
public record FornecedorProdutoListDTO(
        Long id,

        Long fornecedorId,
        String fornecedorNome,

        String codigoFornecedor,
        BigDecimal precoCusto,
        Integer prazoEntregaDias
) {}
