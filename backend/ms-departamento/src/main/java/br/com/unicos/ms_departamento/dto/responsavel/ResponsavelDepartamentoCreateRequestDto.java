package br.com.unicos.ms_departamento.dto;

import br.com.unicos.ms_departamento.enums.PapelResponsavelDepartamento;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação de {@code ResponsavelDepartamento}.
 * <p>
 * Contém os campos necessários para cadastrar o responsável no MVP,
 * mantendo a integração por identificadores lógicos.
 *
 * @param departamentoId               Identificador do departamento.
 * @param responsavelId                Identificador do responsável (ms-pessoas/ms-rh).
 * @param papel                        Papel do responsável.
 * @param principal                    Indica se é responsável principal.
 * @param vigenciaInicio               Início da vigência.
 * @param vigenciaFim                  Fim da vigência (opcional).
 * @param statusResponsavelDepartamento Status do vínculo.
 */
public record ResponsavelDepartamentoCreateRequestDto(
        @NotNull Long departamentoId,
        @NotNull Long responsavelId,
        @NotNull PapelResponsavelDepartamento papel,
        @NotNull Boolean principal,
        @NotNull LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        @NotNull StatusResponsavelDepartamento statusResponsavelDepartamento
) { }
