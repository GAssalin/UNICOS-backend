package br.com.unicos.core.pedido.model;

import br.com.unicos.core.pedido.enums.StatusEntrega;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa as informações de entrega associadas a um pedido.
 *
 * <p>
 * Inclui dados sobre prazos, status logístico e controle de recebimento.
 * </p>
 */
@Entity
@Table(name = "entrega_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntregaPedido {

    /**
     * Identificador único da entrega.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do pedido associado à entrega.
     */
    @Column(nullable = false)
    private Long pedidoId;

    /**
     * Data prevista para entrega ao cliente.
     */
    private LocalDateTime dataPrevista;

    /**
     * Data real de conclusão da entrega.
     */
    private LocalDateTime dataEntrega;

    /**
     * Status atual da entrega (ex: AGUARDANDO, EM_TRANSITO, ENTREGUE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEntrega status;
}
