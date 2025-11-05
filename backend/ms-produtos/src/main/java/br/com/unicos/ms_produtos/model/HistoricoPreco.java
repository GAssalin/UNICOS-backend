package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que registra o histórico de alterações de preço de um produto.
 * Cada registro guarda o preço anterior, o novo preço, a data da alteração e o motivo.
 */
@Entity
@Table(name = "historico_preco")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal precoAnterior;

    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal novoPreco;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @Column(length = 255)
    private String motivo;

    @PrePersist
    public void prePersist() {
        if (dataAlteracao == null) {
            dataAlteracao = LocalDateTime.now();
        }
    }
}
