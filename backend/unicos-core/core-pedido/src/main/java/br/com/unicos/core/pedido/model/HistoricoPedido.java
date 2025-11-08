package br.com.unicos.core.pedido.model;

import br.com.unicos.core.pedido.enums.StatusPedidoBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Registro histórico das alterações de status de um pedido.
 *
 * <p>
 * Permite rastrear o fluxo de processamento do pedido ao longo do tempo,
 * registrando o status anterior, o novo status e o momento da alteração.
 * </p>
 */
@Entity
@Table(name = "historico_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPedido {

    /**
     * Identificador único do registro de histórico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do pedido relacionado.
     */
    @Column(nullable = false)
    private Long pedidoId;

    /**
     * Status anterior do pedido.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusPedidoBase statusAnterior;

    /**
     * Novo status definido para o pedido.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusPedidoBase novoStatus;

    /**
     * Data e hora da atualização.
     */
    @Column(nullable = false)
    private LocalDateTime dataAlteracao;

    /**
     * Identificador do usuário responsável pela alteração.
     */
    private Long usuarioId;
}
