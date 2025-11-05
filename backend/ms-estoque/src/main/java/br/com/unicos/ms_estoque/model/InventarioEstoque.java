package br.com.unicos.ms_estoque.model;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_local_id", nullable = false)
    private EstoqueLocal estoqueLocal;

    @Column(nullable = false)
    private LocalDateTime dataInicio = LocalDateTime.now();

    private LocalDateTime dataFim;

    @Column(length = 50)
    private String status; // Ex: ABERTO, FINALIZADO, CANCELADO
}
