package br.com.unicos.ms_compras.dto.fiscal;

import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para listagem de notas fiscais de compra.
 *
 * <p>Fornece uma visão resumida para exibição em tabelas e relatórios.</p>
 */
public record NotaFiscalCompraListDTO(

        Long id,
        String numeroNota,
        TipoNotaFiscal tipoNotaFiscal,
        StatusNotaFiscalCompra status,
        LocalDate dataEmissao,
        Long fornecedorId,
        BigDecimal valorTotal
) {}
