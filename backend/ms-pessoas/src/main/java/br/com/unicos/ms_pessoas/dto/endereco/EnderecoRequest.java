package br.com.unicos.ms_pessoas.dto.endereco;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de endereços de uma pessoa.
 * <p>
 * Permite cadastrar dados completos de localização, incluindo município e tipo de endereço.
 */
public record EnderecoRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O tipo do endereço é obrigatório.")
        TipoEndereco tipo,

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

        @NotNull(message = "O ID do município é obrigatório.")
        Long municipioId,

        @NotBlank(message = "O CEP é obrigatório.")
        @Size(max = 8, message = "O CEP deve ter no máximo 8 caracteres.")
        String cep,

        Boolean principal
) {}
