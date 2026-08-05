package br.com.unicos.ms_pessoas.dto.relacao;

/**
 * DTO utilizado em listagens de relações entre pessoas,
 * trazendo apenas informações essenciais.
 */
public record PessoaRelacaoListDTO(
        Long id,
        Long pessoaId,
        Long relacionadoId,
        Long tipoRelacaoPessoaId
) {}
