package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.enums.TipoLogradouro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação e atualização de endereços de pessoa.
 */
public record EnderecoPessoaRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O tipo de endereço é obrigatório.")
        TipoEndereco tipoEndereco,

        TipoLogradouro tipoLogradouro,

        @NotBlank @Size(max = 120)
        String logradouro,

        @NotBlank @Size(max = 10)
        String numero,

        @Size(max = 60)
        String complemento,

        @NotBlank @Size(max = 60)
        String bairro,

        @NotBlank
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 00000-000.")
        String cep,

        @NotNull(message = "O município é obrigatório.")
        Long municipioId,

        boolean principal
) {}