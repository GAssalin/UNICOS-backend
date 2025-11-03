package br.com.erp.ms_empresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização de departamentos vinculados à empresa.
 */
public record DepartamentoEmpresaRequest(

        @NotNull(message = "O ID da empresa é obrigatório.")
        Long empresaId,

        @NotBlank(message = "O nome do departamento é obrigatório.")
        @Size(max = 100, message = "O nome do departamento deve ter no máximo 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "O status do departamento é obrigatório.")
        Boolean ativo
) {}