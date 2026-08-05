package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;

import java.time.LocalDate;

/**
 * DTO utilizado para filtros de pesquisa de {@code VinculoEstoqueFilial}.
 *
 * @param estoqueId filtra por estoque.
 * @param filialId filtra por filial.
 * @param tipoAtuacao filtra por tipo de atuação.
 * @param statusVinculoEstoqueFilial filtra por status.
 * @param vigenteEm filtra vínculos vigentes em determinada data.
 */
public record VinculoEstoqueFilialSearchRequestDto(
        Long estoqueId,
        Long filialId,
        TipoAtuacaoEstoque tipoAtuacao,
        StatusVinculoEstoqueFilial statusVinculoEstoqueFilial,
        LocalDate vigenteEm
) { }