package br.com.unicos.ms_compras.dto.fiscal;

import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de resposta detalhada da Nota Fiscal de Compra.
 */
public record NotaFiscalCompraResponse(

        Long id,
        String numeroNota,
        String serie,
        String chaveAcesso,
        TipoNotaFiscal tipoNotaFiscal,
        StatusNotaFiscalCompra status,
        LocalDate dataEmissao,
        LocalDate dataEntrada,
        Long fornecedorId,
        BigDecimal valorTotal,
        BigDecimal valorImpostos,
        String observacao,
        Long pedidoCompraId,

        /**
         * Lista de itens da nota fiscal.
         */
        List<NotaFiscalItemResumoDTO> itens
) {}
