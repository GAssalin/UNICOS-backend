package br.com.unicos.ms_produtos.dto.fornecedor_produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta que representa um fornecedor associado a um produto.
 *
 * <p>
 * Retorna as informações completas do vínculo fornecedor–produto,
 * incluindo dados operacionais e de auditoria.
 * </p>
 */
public record FornecedorProdutoResponse(
        Long id,

        Long fornecedorId,
        String fornecedorNome,

        String codigoFornecedor,
        BigDecimal precoCusto,
        Integer prazoEntregaDias,

        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
