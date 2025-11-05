package br.com.unicos.ms_ativos.dto;

/**
 * DTO simplificado para listagem de localizações.
 * <p>
 * Usado em consultas de tabela, relatórios e filtros.
 */
public record LocalizacaoListDTO(

        /** Identificador único da localização. */
        Long id,

        /** Descrição da localização. */
        String descricao,

        /** Identificação do andar. */
        String andar,

        /** Identificação do bloco. */
        String bloco
) { }
