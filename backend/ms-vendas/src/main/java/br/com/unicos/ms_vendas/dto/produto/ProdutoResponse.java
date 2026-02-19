package br.com.unicos.ms_vendas.dto.produto;

import br.com.unicos.ms_vendas.enums.TipoProduto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para {@code Produto}.
 */
public record ProdutoResponse(

        Long id,
        String codigo,
        String nome,
        String descricao,
        TipoProduto tipoProduto,
        Long unidadeMedidaId,
        Long categoriaId,
        Long marcaId,
        String codigoBarras,
        BigDecimal precoBase,
        BigDecimal peso,
        BigDecimal volume,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm

) {}
