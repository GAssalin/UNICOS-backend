package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.ms_pedido.enums.StatusPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa os pagamentos relacionados a um pedido.
 * <p>
 * Observação: o método de pagamento é armazenado como código textual
 * vindo do ms-pagamento (ex.: "PIX", "CARTAO_CREDITO", "BOLETO").
 * Evita acoplamento de enum entre microserviços.
 */
@Entity
@Table(name = "pagamento_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(length = 50, nullable = false)
    private String codigoTransacao; // vindo do ms-pagamento

    @Column(length = 30)
    private String metodoPagamento; // PIX, CARTAO, BOLETO...

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorPago;

    private LocalDateTime dataPagamento;
}
