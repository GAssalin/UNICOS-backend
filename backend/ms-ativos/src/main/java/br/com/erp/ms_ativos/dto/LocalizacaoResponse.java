package br.com.erp.ms_ativos.dto;

/**
 * DTO usado para retorno detalhado de localizações.
 */
public record LocalizacaoResponse(
        Long id,
        String descricao,
        String andar,
        String bloco,
        Long filialId
) {}