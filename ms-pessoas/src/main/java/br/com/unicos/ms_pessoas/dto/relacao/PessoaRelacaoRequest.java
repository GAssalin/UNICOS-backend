package br.com.unicos.ms_pessoas.dto.relacao;

import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação ou atualização de relações entre pessoas.
 * <p>
 * Representa vínculos como pai, mãe, dependente, sócio, representante legal,
 * responsável financeiro, tutor, entre outros.
 */
public record PessoaRelacaoRequest(

        @NotNull(message = "O ID da pessoa principal é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O ID da pessoa relacionada é obrigatório.")
        Long relacionadoId,

        @NotNull(message = "O tipo da relação é obrigatório.")
        Long tipoRelacaoPessoaId
) {}
