package br.com.erp.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Produto produto;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoAnterior;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal novoPreco;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @Column(length = 255)
    private String motivo;

}
