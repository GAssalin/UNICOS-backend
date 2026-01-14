package br.com.unicos.ms_filial.dto.parametro;

/**
 * DTO de resposta de FilialParametro.
 */
public record FilialParametroResponse(
        Long id,
        Long filialId,
        String chave,
        String valor,
        String descricao,
        Boolean ativo
) { }
