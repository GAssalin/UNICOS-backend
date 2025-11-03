package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO usado para retorno detalhado de ativo.
 */
public record AtivoResponse(
        Long id,
        String nome,
        String codigoPatrimonial,
        String descricao,
        TipoAtivo tipo,
        StatusAtivo status,
        LocalDate dataAquisicao,
        BigDecimal valorAquisicao,
        BigDecimal valorAtual,
        Long empresaId,
        Long filialId,
        Long responsavelId,
        Long localizacaoId
) {}