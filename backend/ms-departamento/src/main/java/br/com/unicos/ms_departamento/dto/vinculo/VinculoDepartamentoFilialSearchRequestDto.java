package br.com.unicos.ms_departamento.dto.vinculo;

import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.enums.TipoAtuacaoDepartamento;

import java.time.LocalDate;

/**
 * DTO de filtro/pesquisa para listagem de {@code VinculoDepartamentoFilial}.
 * <p>
 * Útil para endpoints de consulta com paginação.
 *
 * @param departamentoId                 Filtra por departamento (opcional).
 * @param filialId                       Filtra por filial (opcional).
 * @param tipoAtuacao                    Filtra por tipo de atuação (opcional).
 * @param statusVinculoDepartamentoFilial Filtra por status (opcional).
 * @param vigenteEm                      Quando informado, filtra vínculos vigentes na data.
 */
public record VinculoDepartamentoFilialSearchRequestDto(
        Long departamentoId,
        Long filialId,
        TipoAtuacaoDepartamento tipoAtuacao,
        StatusVinculoDepartamentoFilial statusVinculoDepartamentoFilial,
        LocalDate vigenteEm
) { }
