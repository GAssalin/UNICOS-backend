package br.com.unicos.ms_pessoas.dto.relacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de tipos de relação entre pessoas.
 * <p>
 * Exemplos: Pai, Mãe, Dependente, Sócio, Representante Legal, entre outros.
 */
public record TipoRelacaoPessoaRequest(

        @NotBlank(message = "O nome do tipo de relação é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao
) {}
