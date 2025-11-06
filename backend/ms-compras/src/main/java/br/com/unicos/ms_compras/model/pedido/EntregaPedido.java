package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.ms_pedido.enums.StatusEntrega;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa a entrega associada a um pedido.
 */
@Entity
@Table(name = "entrega_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntregaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(length = 60)
    private String codigoRastreio;

    @Column(length = 100)
    private String transportadora;

    private LocalDateTime dataEnvio;

    private LocalDateTime dataEntrega;

    @Enumerated(EnumType.STRING)
    private StatusEntrega status;
}
