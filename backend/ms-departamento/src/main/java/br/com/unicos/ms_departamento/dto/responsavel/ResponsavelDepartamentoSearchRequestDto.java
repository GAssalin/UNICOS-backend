package br.com.unicos.ms_departamento.dto;

import br.com.unicos.ms_departamento.enums.PapelResponsavelDepartamento;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;

import java.time.LocalDate;

/**
 * DTO de filtro/pesquisa para listagem de {@code ResponsavelDepartamento}.
 * <p>
 * Útil para endpoints de consulta com paginação.
 *
 * @param departamentoId               Filtra por departamento (opcional).
 * @param responsavelId                Filtra por responsável (opcional).
 * @param papel                        Filtra por papel (opcional).
 * @param principal                    Filtra por principal (opcional).
 * @param statusResponsavelDepartamento Filtra por status (opcional).
 * @param vigenteEm                    Quando informado, filtra vínculos vigentes na data.
 */
public record ResponsavelDepartamentoSearchRequestDto(
        Long departamentoId,
        Long responsavelId,
        PapelResponsavelDepartamento papel,
        Boolean principal,
        StatusResponsavelDepartamento statusResponsavelDepartamento,
        LocalDate vigenteEm
) { }
