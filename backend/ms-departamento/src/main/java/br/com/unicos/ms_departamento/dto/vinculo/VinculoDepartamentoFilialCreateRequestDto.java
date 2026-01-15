package br.com.unicos.ms_departamento.dto.vinculo;

import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.enums.TipoAtuacaoDepartamento;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação de {@code VinculoDepartamentoFilial}.
 * <p>
 * Contém os campos necessários para vincular um departamento a uma filial no MVP,
 * mantendo integrações por id lógico.
 *
 * @param departamentoId                 Identificador do departamento.
 * @param filialId                       Identificador da filial.
 * @param tipoAtuacao                    Tipo de atuação do departamento na filial.
 * @param vigenciaInicio                 Início da vigência do vínculo.
 * @param vigenciaFim                    Fim da vigência (opcional).
 * @param statusVinculoDepartamentoFilial Status do vínculo Departamento x Filial.
 */
public record VinculoDepartamentoFilialCreateRequestDto(
        @NotNull Long departamentoId,
        @NotNull Long filialId,
        @NotNull TipoAtuacaoDepartamento tipoAtuacao,
        @NotNull LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        @NotNull StatusVinculoDepartamentoFilial statusVinculoDepartamentoFilial
) { }
