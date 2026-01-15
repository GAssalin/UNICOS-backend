package br.com.unicos.ms_departamento.dto.responsavel;

import br.com.unicos.ms_departamento.enums.PapelResponsavelDepartamento;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;

import java.time.LocalDate;

/**
 * DTO utilizado para retorno (leitura) de {@code ResponsavelDepartamento}.
 * <p>
 * Exposto em listagens e detalhamento, refletindo os dados persistidos
 * e permitindo que outros microserviços consumam a informação por id lógico.
 *
 * @param id                         Identificador do vínculo.
 * @param departamentoId              Identificador do departamento.
 * @param responsavelId               Identificador do responsável (ms-pessoas/ms-rh).
 * @param papel                       Papel do responsável no departamento.
 * @param principal                   Indica se é o responsável principal do departamento.
 * @param vigenciaInicio              Início da vigência.
 * @param vigenciaFim                 Fim da vigência (opcional).
 * @param statusResponsavelDepartamento Status do vínculo do responsável com o departamento.
 */
public record ResponsavelDepartamentoResponseDto(
        Long id,
        Long departamentoId,
        Long responsavelId,
        PapelResponsavelDepartamento papel,
        Boolean principal,
        LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        StatusResponsavelDepartamento statusResponsavelDepartamento
) { }
