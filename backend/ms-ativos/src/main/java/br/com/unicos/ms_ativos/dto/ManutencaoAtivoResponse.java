package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta utilizado para exibição detalhada das informações
 * de uma manutenção realizada em um ativo.
 */
public record ManutencaoAtivoResponse(

        /** Identificador único da manutenção. */
        Long id,

        /** Identificador do ativo ao qual a manutenção pertence. */
        Long ativoId,

        /** Nome do ativo (para exibição em relatórios). */
        String nomeAtivo,

        /** Identificador do fornecedor responsável. */
        Long fornecedorId,

        /** Nome do fornecedor (para exibição em listagens). */
        String nomeFornecedor,

        /** Data em que a manutenção foi realizada ou programada. */
        LocalDate dataManutencao,

        /** Tipo da manutenção: PREVENTIVA ou CORRETIVA. */
        TipoManutencao tipo,

        /** Descrição do serviço realizado ou a realizar. */
        String descricaoServico,

        /** Custo total da manutenção. */
        BigDecimal custo,

        /** Status atual da manutenção. */
        StatusManutencao status
) { }
