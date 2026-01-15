package br.com.unicos.ms_departamento.dto.departamento;

import br.com.unicos.ms_departamento.enums.StatusDepartamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação de {@code Departamento}.
 * <p>
 * Contém apenas os campos necessários para cadastro no MVP.
 *
 * @param codigo             Código interno do departamento (único no tenant).
 * @param nome               Nome do departamento.
 * @param descricao          Descrição livre (opcional).
 * @param statusDepartamento Status do departamento.
 * @param departamentoPaiId  Identificador lógico do departamento pai (opcional).
 */
public record DepartamentoCreateRequestDto(
        @NotBlank String codigo,
        @NotBlank String nome,
        String descricao,
        @NotNull StatusDepartamento statusDepartamento,
        Long departamentoPaiId
) { }
