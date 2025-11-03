package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização dos endereços vinculados a uma empresa.
 */
public record EnderecoEmpresaRequest(

        @NotNull(message = "O ID da empresa é obrigatório.")
        Long empresaId,

        @NotBlank(message = "O logradouro é obrigatório.")
        @Size(max = 150, message = "O logradouro deve ter no máximo 150 caracteres.")
        String logradouro,

        @NotBlank(message = "O número é obrigatório.")
        @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
        String numero,

        @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres.")
        String complemento,

        @NotBlank(message = "O bairro é obrigatório.")
        @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
        String bairro,

        @NotBlank(message = "A cidade é obrigatória.")
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
        String cidade,

        @NotBlank(message = "A UF é obrigatória.")
        @Size(max = 2, message = "A UF deve ter no máximo 2 caracteres.")
        String uf,

        @NotBlank(message = "O CEP é obrigatório.")
        @Size(max = 10, message = "O CEP deve ter no máximo 10 caracteres.")
        String cep,

        @NotNull(message = "O tipo de endereço é obrigatório.")
        TipoEnderecoEmpresa tipo
) {}