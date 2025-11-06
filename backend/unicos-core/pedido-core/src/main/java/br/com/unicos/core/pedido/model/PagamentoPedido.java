package br.com.unicos.core.pedido.model;

import br.com.unicos.core.pedido.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa o registro de pagamento vinculado a um pedido.
 *
 * <p>
 * Contém as informações financeiras relacionadas à quitação do pedido,
 * incluindo forma de pagamento, valor e status.
 * </p>
 */
@Entity
@Table(name = "pagamento_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoPedido {

    /**
     * Identificador único do pagamento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Valor pago no pedido.
     */
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorPago;

    /**
     * Data e hora em que o pagamento foi efetuado.
     */
    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    /**
     * Forma de pagamento utilizada (ex: cartão, boleto, PIX).
     */
    @Column(length = 30)
    private String formaPagamento;

    /**
     * Status atual do pagamento.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status;
}
