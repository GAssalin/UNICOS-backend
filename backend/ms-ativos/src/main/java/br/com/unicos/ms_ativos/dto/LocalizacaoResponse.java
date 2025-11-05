package br.com.unicos.ms_ativos.dto;

import java.util.List;

/**
 * DTO de resposta utilizado para exibir informações detalhadas
 * sobre a localização física de um ativo.
 */
public record LocalizacaoResponse(

        /** Identificador único da localização. */
        Long id,

        /** Descrição da localização (ex: "Depósito Central"). */
        String descricao,

        /** Identificação do andar. */
        String andar,

        /** Identificação do bloco ou prédio. */
        String bloco,

        /** Identificador da filial associada (referência ao ms-empresa). */
        Long filialId,

        /** Lista resumida de ativos vinculados a esta localização. */
        List<AtivoListDTO> ativos
) { }
