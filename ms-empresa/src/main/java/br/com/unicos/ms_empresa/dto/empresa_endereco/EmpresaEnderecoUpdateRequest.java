package br.com.unicos.ms_empresa.dto.empresa_endereco;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para atualização de um endereço institucional da empresa.
 */
public record EmpresaEnderecoUpdateRequest(
        @NotBlank
        String logradouro,
        @NotBlank
        String numero,
        String complemento,
        @NotBlank
        String bairro,
        @NotBlank
        String municipio,
        @NotBlank
        String uf,
        @NotBlank
        String cep,
        boolean principal
) { }
