package br.com.unicos.ms_departamento.dto.departamento;

import br.com.unicos.ms_departamento.enums.StatusDepartamento;

/**
 * DTO utilizado para retorno (leitura) de {@code Departamento}.
 * <p>
 * Exposto em listagens e detalhamento, refletindo os dados persistidos
 * e facilitando integrações com outros microserviços.
 *
 * @param id                 Identificador do departamento.
 * @param codigo             Código interno do departamento.
 * @param nome               Nome do departamento.
 * @param descricao          Descrição livre do departamento.
 * @param statusDepartamento Status do departamento.
 * @param departamentoPaiId  Identificador lógico do departamento pai (opcional).
 */
public record DepartamentoResponseDto(
        Long id,
        String codigo,
        String nome,
        String descricao,
        StatusDepartamento statusDepartamento,
        Long departamentoPaiId
) { }
