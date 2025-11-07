package br.com.unicos.ms_compras.dto.recebimento;

import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para criação ou atualização de um processo de recebimento de compra.
 *
 * <p>Contém os dados principais do recebimento e seus itens.</p>
 */
public record RecebimentoCompraRequest(

        /**
         * Código identificador do recebimento.
         */
        @NotBlank(message = "O código do recebimento é obrigatório.")
        String codigo,

        /**
         * Tipo do recebimento (total, parcial, devolvido, etc.).
         */
        @NotNull(message = "O tipo de recebimento é obrigatório.")
        TipoRecebimento tipoRecebimento,

        /**
         * Status atual do processo de recebimento.
         */
        @NotNull(message = "O status do recebimento é obrigatório.")
        StatusRecebimentoCompra status,

        /**
         * Data em que o recebimento foi iniciado.
         */
        @NotNull(message = "A data de recebimento é obrigatória.")
        LocalDate dataRecebimento,

        /**
         * Data de conclusão do recebimento (caso aplicável).
         */
        LocalDate dataConclusao,

        /**
         * Observações gerais sobre o recebimento.
         */
        String observacao,

        /**
         * Identificador do pedido de compra vinculado.
         */
        @NotNull(message = "O identificador do pedido de compra é obrigatório.")
        Long pedidoCompraId,

        /**
         * Identificador da nota fiscal associada (opcional).
         */
        Long notaFiscalCompraId,

        /**
         * Itens recebidos neste processo.
         */
        List<RecebimentoItemRequest> itens
) {}
