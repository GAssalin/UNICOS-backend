package br.com.unicos.ms_compras.dto;

import java.time.LocalDate;

/**
 * DTO de documento de entrada vinculado ao recebimento.
 */
public record DocumentoEntradaDto(
        Long id,
        Long recebimentoCompraId,
        String tipoDocumento,
        String numero,
        String serie,
        String chaveAcesso,
        LocalDate dataEmissao,
        String arquivoRef,
        String observacao
) { }
