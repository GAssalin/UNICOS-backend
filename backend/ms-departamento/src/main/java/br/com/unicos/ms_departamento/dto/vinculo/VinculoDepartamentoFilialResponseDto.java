package br.com.unicos.ms_departamento.dto.vinculo;

import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.enums.TipoAtuacaoDepartamento;

import java.time.LocalDate;

/**
 * DTO utilizado para retorno (leitura) de {@code VinculoDepartamentoFilial}.
 * <p>
 * Exposto em listagens e detalhamento, refletindo os dados persistidos
 * e permitindo integrações por identificadores lógicos (sem FK física).
 *
 * @param id                             Identificador do vínculo.
 * @param departamentoId                  Identificador do departamento (ms-departamento).
 * @param filialId                        Identificador da filial (ms-filial).
 * @param tipoAtuacao                     Tipo de atuação do departamento na filial.
 * @param vigenciaInicio                  Início da vigência do vínculo.
 * @param vigenciaFim                     Fim da vigência (opcional).
 * @param statusVinculoDepartamentoFilial  Status do vínculo Departamento x Filial.
 */
public record VinculoDepartamentoFilialResponseDto(
        Long id,
        Long departamentoId,
        Long filialId,
        TipoAtuacaoDepartamento tipoAtuacao,
        LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        StatusVinculoDepartamentoFilial statusVinculoDepartamentoFilial
) { }
