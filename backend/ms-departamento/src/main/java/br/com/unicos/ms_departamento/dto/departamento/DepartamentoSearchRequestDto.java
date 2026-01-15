package br.com.unicos.ms_departamento.dto.departamento;

import br.com.unicos.ms_departamento.enums.StatusDepartamento;

/**
 * DTO de filtro/pesquisa para listagem de {@code Departamento}.
 * <p>
 * Útil para endpoints de consulta com paginação.
 *
 * @param codigo             Código interno (opcional, busca exata ou por prefixo conforme implementação).
 * @param nome               Nome (opcional, busca por contains/like conforme implementação).
 * @param statusDepartamento Status do departamento (opcional).
 * @param departamentoPaiId  Filtra por departamento pai (opcional).
 * @param apenasRaiz         Quando {@code true}, retorna somente departamentos sem pai.
 */
public record DepartamentoSearchRequestDto(
        String codigo,
        String nome,
        StatusDepartamento statusDepartamento,
        Long departamentoPaiId,
        Boolean apenasRaiz
) { }
