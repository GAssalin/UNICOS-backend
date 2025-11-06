package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.ms_pedido.enums.StatusPedido;
import br.com.unicos.ms_pedido.enums.TipoPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um pedido de venda.
 * <p>
 * Contém informações gerais sobre o cliente, valor total,
 * forma de pagamento, status e data de criação.
 */
@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, unique = true, nullable = false)
    private String codigo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoPedido tipo;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal desconto;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @NotNull
    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = true)
    private Long enderecoEntregaId;

    @Column(nullable = true)
    private Long usuarioResponsavelId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();
}
