package br.com.unicos.ms_pessoas.dto.relacao;

/**
 * DTO de retorno que representa uma relação completa entre duas pessoas.
 */
public record PessoaRelacaoResponse(
        Long id,
        Long pessoaId,
        Long relacionadoId,
        Long tipoRelacaoPessoaId
) {}
