package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.TipoTransferencia;

import java.time.LocalDate;

/**
 * DTO de resposta utilizado para exibir informações detalhadas
 * sobre a movimentação (transferência) de um ativo.
 */
public record TransferenciaAtivoResponse(

        /** Identificador único da transferência. */
        Long id,

        /** Identificador do ativo transferido. */
        Long ativoId,

        /** Nome do ativo transferido (para exibição em relatórios). */
        String nomeAtivo,

        /** Identificador da unidade de origem (referência ao ms-empresa). */
        Long origemId,

        /** Nome ou descrição da unidade de origem (para exibição em relatórios). */
        String nomeOrigem,

        /** Identificador da unidade de destino (referência ao ms-empresa). */
        Long destinoId,

        /** Nome ou descrição da unidade de destino (para exibição em relatórios). */
        String nomeDestino,

        /** Tipo da transferência (INTERNA, ENTRE_FILIAIS, BAIXA, OUTROS). */
        TipoTransferencia tipo,

        /** Data da transferência. */
        LocalDate dataTransferencia,

        /** Identificador do usuário responsável (referência ao ms-pessoas). */
        Long responsavelId,

        /** Motivo ou observação da transferência. */
        String motivo
) { }
