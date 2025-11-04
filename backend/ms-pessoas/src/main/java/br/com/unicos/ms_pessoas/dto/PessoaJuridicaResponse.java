package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import java.time.LocalDateTime;

/**
 * DTO de retorno para pessoa jurídica.
 */
public record PessoaJuridicaResponse(
        Long id,
        String nome,
        TipoPessoa tipoPessoa,
        String cnpj,
        String nomeFantasia,
        String inscricaoEstadual,
        String inscricaoMunicipal,
        boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}