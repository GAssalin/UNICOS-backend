package br.com.unicos.ms_compras.dto.fiscal;

import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para criação ou atualização de uma Nota Fiscal de Compra.
 */
public record NotaFiscalCompraRequest(

        /**
         * Número da nota fiscal.
         */
        @NotBlank(message = "O número da nota fiscal é obrigatório.")
        String numeroNota,

        /**
         * Série da nota fiscal.
         */
        String serie,

        /**
         * Chave de acesso da nota fiscal eletrônica (NFe).
         */
        @NotBlank(message = "A chave de acesso é obrigatória.")
        String chaveAcesso,

        /**
         * Tipo da nota fiscal (entrada, devolução, complementar, etc.).
         */
        @NotNull(message = "O tipo da nota fiscal é obrigatório.")
        TipoNotaFiscal tipoNotaFiscal,

        /**
         * Status atual da nota fiscal.
         */
        @NotNull(message = "O status da nota fiscal é obrigatório.")
        StatusNotaFiscalCompra status,

        /**
         * Data de emissão da nota fiscal.
         */
        @NotNull(message = "A data de emissão é obrigatória.")
        LocalDate dataEmissao,

        /**
         * Data de entrada dos produtos no estoque.
         */
        LocalDate dataEntrada,

        /**
         * Identificador do fornecedor (FK futura para ms-pessoas).
         */
        @NotNull(message = "O identificador do fornecedor é obrigatório.")
        Long fornecedorId,

        /**
         * Valor total da nota fiscal.
         */
        @NotNull(message = "O valor total é obrigatório.")
        @Positive(message = "O valor total deve ser maior que zero.")
        BigDecimal valorTotal,

        /**
         * Valor total dos impostos incidentes.
         */
        BigDecimal valorImpostos,

        /**
         * Observações gerais sobre a nota fiscal.
         */
        String observacao,

        /**
         * Identificador do pedido de compra associado.
         */
        Long pedidoCompraId,

        /**
         * Itens que compõem a nota fiscal.
         */
        List<NotaFiscalItemRequest> itens
) {}
