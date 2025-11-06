package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.ms_pedido.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que registra o histórico de alterações de status do pedido.
 */
@Entity
@Table(name = "historico_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    private LocalDateTime dataEvento;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusAtual;

    @Column(length = 255)
    private String observacao;

    private Long usuarioId;
}
