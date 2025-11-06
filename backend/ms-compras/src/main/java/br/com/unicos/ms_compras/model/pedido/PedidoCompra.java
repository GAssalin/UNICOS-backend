package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.core.pedido.model.Pedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PedidoCompra extends Pedido {

    private LocalDate dataPrevistaEntrega;

    @NotNull
    @Column(nullable = false)
    private Long filialId;

    @NotNull
    @Column(nullable = false)
    private Long fornecedorId;

    @Column(nullable = false)
    private Long compradorId;

    @Column
    private Long cotacaoId;

    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PedidoItem> itens = new ArrayList<>();
}
