package br.com.unicos.ms_estoque.dto.movimentacao;

import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;

import java.time.LocalDateTime;

/**
 * DTO utilizado para filtros de pesquisa de {@code MovimentacaoEstoque}.
 *
 * @param tipoMovimentacao filtra por tipo de movimentação.
 * @param estoqueOrigemId filtra por estoque de origem.
 * @param estoqueDestinoId filtra por estoque de destino.
 * @param documentoReferencia filtra por documento de referência.
 * @param usuarioResponsavelId filtra por usuário responsável.
 * @param statusMovimentacao filtra por status da movimentação.
 * @param dataInicial filtra movimentações a partir desta data/hora.
 * @param dataFinal filtra movimentações até esta data/hora.
 */
public record MovimentacaoEstoqueSearchRequestDto(
        TipoMovimentacaoEstoque tipoMovimentacao,
        Long estoqueOrigemId,
        Long estoqueDestinoId,
        String documentoReferencia,
        Long usuarioResponsavelId,
        StatusMovimentacaoEstoque statusMovimentacao,
        LocalDateTime dataInicial,
        LocalDateTime dataFinal
) { }