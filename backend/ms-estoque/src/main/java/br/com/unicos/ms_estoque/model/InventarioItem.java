package br.com.unicos.ms_estoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Itens contados durante o processo de inventário físico.
 */
@Entity
@Table(name = "inventario_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id", nullable = false)
    private InventarioEstoque inventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_estoque_id", nullable = false)
    private ProdutoEstoque produtoEstoque;

    @Column(nullable = false)
    private Double quantidadeContada;

    @Column(nullable = false)
    private Double quantidadeRegistrada;

    @Column(length = 255)
    private String observacao;
}
