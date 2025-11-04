package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import java.time.LocalDateTime;

/**
 * DTO de retorno com os dados básicos de uma pessoa.
 */
public record PessoaResponse(
        Long id,
        String nome,
        TipoPessoa tipoPessoa,
        boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}