package br.com.unicos.ms_ativos.dto;

/**
 * DTO usado para listagem simplificada de localizações.
 */
public record LocalizacaoListDTO(
        Long id,
        String descricao,
        String andar
) {}