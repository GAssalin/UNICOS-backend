package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.TipoTransferencia;

import java.time.LocalDate;

/**
 * DTO simplificado para listagem de transferências de ativos.
 * <p>
 * Usado em consultas de tabela, dashboards e relatórios resumidos.
 */
public record TransferenciaAtivoListDTO(

        /** Identificador único da transferência. */
        Long id,

        /** Nome do ativo transferido. */
        String nomeAtivo,

        /** Tipo da transferência. */
        TipoTransferencia tipo,

        /** Data em que a transferência foi registrada. */
        LocalDate dataTransferencia,

        /** Nome ou descrição da unidade de origem. */
        String nomeOrigem,

        /** Nome ou descrição da unidade de destino. */
        String nomeDestino
) { }
