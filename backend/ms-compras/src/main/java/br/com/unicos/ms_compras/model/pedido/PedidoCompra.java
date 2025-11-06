package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.core.pedido.model.Pedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entidade que representa o pedido de compra dentro do módulo de Compras.
 *
 * <p>
 * Estende {@link Pedido} do módulo core, herdando os atributos e comportamentos
 * compartilhados (status, tipo, valores, datas, etc.).
 * </p>
 */
@Entity
@Table(name = "pedido_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PedidoCompra extends Pedido {

    /**
     * Identificador do fornecedor responsável por este pedido.
     */
    @Column(name = "fornecedor_id", nullable = false)
    private Long fornecedorId;

    /**
     * Observações específicas da compra.
     */
    @Column(length = 500)
    private String observacao;
}