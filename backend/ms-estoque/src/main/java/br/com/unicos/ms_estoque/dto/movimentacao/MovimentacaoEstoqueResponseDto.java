package br.com.unicos.ms_estoque.dto.movimentacao;

import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;

import java.time.LocalDateTime;

/**
 * DTO utilizado para retorno de dados de {@code MovimentacaoEstoque}.
 *
 * @param id identificador da movimentação.
 * @param tipoMovimentacao tipo da movimentação.
 * @param estoqueOrigemId identificador do estoque de origem.
 * @param estoqueDestinoId identificador do estoque de destino.
 * @param dataMovimentacao data e hora da movimentação.
 * @param observacao observação da movimentação.
 * @param documentoReferencia documento de referência da movimentação.
 * @param usuarioResponsavelId identificador do usuário responsável.
 * @param statusMovimentacao status da movimentação.
 */
public record MovimentacaoEstoqueResponseDto(
        Long id,
        TipoMovimentacaoEstoque tipoMovimentacao,
        Long estoqueOrigemId,
        Long estoqueDestinoId,
        LocalDateTime dataMovimentacao,
        String observacao,
        String documentoReferencia,
        Long usuarioResponsavelId,
        StatusMovimentacaoEstoque statusMovimentacao
) { }