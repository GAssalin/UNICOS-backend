package br.com.erp.ms_ativos.model;

import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;
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

@Entity
@Table(name = "manutencao_ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManutencaoAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    @PastOrPresent
    @Column(name = "data_manutencao", nullable = false)
    private LocalDate dataManutencao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoManutencao tipo;

    @Column(name = "descricao_servico", length = 255)
    private String descricaoServico;

    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal custo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusManutencao status;
}