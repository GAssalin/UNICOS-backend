package br.com.unicos.ms_estoque.model;

import br.com.unicos.ms_estoque.enums.StatusInventario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Cabeçalho de um processo de inventário físico de estoque.
 */
@Entity
@Table(name = "inventario_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Local de estoque ao qual o inventário pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_local_id", nullable = false)
    private EstoqueLocal estoqueLocal;

    /**
     * Data de início do inventário físico.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataInicio = LocalDateTime.now();

    /**
     * Data de finalização do inventário (quando aplicável).
     */
    private LocalDateTime dataFim;

    /**
     * Status atual do processo de inventário (aberto, em andamento, finalizado, cancelado).
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    @Builder.Default
    private StatusInventario status = StatusInventario.ABERTO;
}
