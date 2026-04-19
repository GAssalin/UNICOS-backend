package br.com.unicos.ms_empresa.dto.empresa_endereco;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação de um endereço institucional da empresa.
 */
public record EmpresaEnderecoCreateRequest(
        @NotNull
        TipoEnderecoEmpresa tipoEndereco,
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