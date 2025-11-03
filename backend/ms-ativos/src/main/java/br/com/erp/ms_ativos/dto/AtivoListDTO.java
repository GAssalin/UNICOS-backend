package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;

/**
 * DTO usado para listagem simplificada de ativos.
 */
public record AtivoListDTO(
        Long id,
        String nome,
        String codigoPatrimonial,
        TipoAtivo tipo,
        StatusAtivo status
) {}