package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;

import java.time.LocalDate;

/**
 * DTO de filtro/pesquisa para listagem de {@code VinculoEstoqueFilial}.
 * <p>
 * Útil para endpoints de consulta com paginação.
 *
 * @param estoqueId                 Filtra por estoque (opcional).
 * @param filialId                       Filtra por filial (opcional).
 * @param tipoAtuacao                    Filtra por tipo de atuação (opcional).
 * @param statusVinculoEstoqueFilial Filtra por status (opcional).
 * @param vigenteEm                      Quando informado, filtra vínculos vigentes na data.
 */
public record VinculoEstoqueFilialSearchRequestDto(
        Long estoqueId,
        Long filialId,
        TipoAtuacaoEstoque tipoAtuacao,
        StatusVinculoEstoqueFilial statusVinculoEstoqueFilial,
        LocalDate vigenteEm
) { }
