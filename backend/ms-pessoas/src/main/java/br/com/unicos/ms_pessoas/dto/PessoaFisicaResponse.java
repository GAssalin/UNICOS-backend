package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import java.time.LocalDateTime;

/**
 * DTO de retorno para pessoa física.
 */
public record PessoaFisicaResponse(
        Long id,
        String nome,
        TipoPessoa tipoPessoa,
        String cpf,
        String rg,
        String dataNascimento,
        String genero,
        boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}