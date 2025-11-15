package br.com.unicos.ms_produtos.dto.fornecedor_produto;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagens de vínculos entre fornecedores e produtos.
 *
 * <p>
 * Fornece uma visão resumida contendo apenas os dados essenciais
 * para exibição em listas, sem informações de auditoria ou detalhes
 * específicos do produto.
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
