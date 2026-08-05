package br.com.unicos.ms_pessoas.dto.relacao;

/**
 * DTO de retorno que representa um tipo de relação completo.
 */
public record TipoRelacaoPessoaResponse(
        Long id,
        String nome,
        String descricao
) {}
