package br.com.unicos.ms_compras.dto.pedido;

import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de um pedido de compra.
 *
 * <p>Contém as informações básicas necessárias para registrar ou atualizar
 * um pedido dentro do módulo de Compras.</p>
 */
public record PedidoCompraRequest(

        /**
         * Identificador do fornecedor responsável pelo pedido.
         */
        @NotNull(message = "O identificador do fornecedor é obrigatório.")
        Long fornecedorId,

        /**
         * Status atual do pedido de compra.
         */
        @NotNull(message = "O status do pedido é obrigatório.")
        StatusPedidoCompra status,

        /**
         * Observações específicas da compra.
         */
        @Size(max = 500, message = "A observação deve conter no máximo 500 caracteres.")
        String observacao
) {}
