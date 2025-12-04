package br.com.unicos.ms_produtos.dto.fornecedor_produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta que representa um fornecedor associado a um produto.
 *
 * <p>
 * Retorna informações completas do vínculo, incluindo auditoria
 * e os dados operacionais cadastrados.
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
