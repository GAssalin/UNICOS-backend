package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;

import java.math.BigDecimal;

/**
 * DTO simplificado para listagem de ativos.
 * <p>
 * Usado em consultas de tabela e listagens resumidas.
 */
public record AtivoListDTO(

        /** Identificador único do ativo. */
        Long id,

        /** Nome do ativo. */
        String nome,

        /** Código patrimonial. */
        String codigoPatrimonial,

        /** Tipo do ativo. */
        TipoAtivo tipo,

        /** Status atual do ativo. */
        StatusAtivo status,

        /** Valor atual do ativo. */
        BigDecimal valorAtual
) { }
