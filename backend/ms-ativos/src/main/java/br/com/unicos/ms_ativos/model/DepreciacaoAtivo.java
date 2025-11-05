package br.com.unicos.ms_ativos.model;

import br.com.unicos.ms_ativos.enums.TipoDepreciacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que registra as depreciações periódicas aplicadas ao ativo
 * para fins contábeis e de controle patrimonial.
 */
@Entity
@Table(name = "depreciacao_ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepreciacaoAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ativo ao qual esta depreciação pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    /**
     * Tipo de depreciação (LINEAR, ACELERADA, REAVALIACAO).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDepreciacao tipo;

    /**
     * Data da competência da depreciação.
     */
    @PastOrPresent
    @Column(name = "data_competencia", nullable = false)
    private LocalDate dataCompetencia;

    /**
     * Valor depreciado neste período.
     */
    @DecimalMin("0.0")
    @Column(name = "valor_depreciado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorDepreciado;

    /**
     * Saldo contábil após a aplicação da depreciação.
     */
    @DecimalMin("0.0")
    @Column(name = "saldo_contabil", precision = 12, scale = 2)
    private BigDecimal saldoContabil;
}
