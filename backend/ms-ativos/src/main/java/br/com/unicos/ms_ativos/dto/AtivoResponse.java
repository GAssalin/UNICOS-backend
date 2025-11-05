package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta utilizado para exibição de informações detalhadas de um ativo.
 */
public record AtivoResponse(

        /** Identificador único do ativo. */
        Long id,

        /** Nome identificador do ativo. */
        String nome,

        /** Código patrimonial do ativo. */
        String codigoPatrimonial,

        /** Descrição detalhada do ativo. */
        String descricao,

        /** Tipo do ativo. */
        TipoAtivo tipo,

        /** Status atual do ativo. */
        StatusAtivo status,

        /** Data de aquisição. */
        LocalDate dataAquisicao,

        /** Valor de aquisição do ativo. */
        BigDecimal valorAquisicao,

        /** Valor atual do ativo (após depreciações). */
        BigDecimal valorAtual,

        /** Empresa proprietária do ativo. */
        Long empresaId,

        /** Filial onde o ativo está alocado. */
        Long filialId,

        /** Colaborador responsável pelo ativo. */
        Long responsavelId,

        /** Localização física (descrição resumida). */
        String localizacaoDescricao
) { }
