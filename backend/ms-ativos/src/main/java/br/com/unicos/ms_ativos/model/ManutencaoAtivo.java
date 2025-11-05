package br.com.unicos.ms_ativos.model;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
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
 * Entidade que representa uma manutenção preventiva ou corretiva realizada em um ativo.
 * <p>
 * Cada manutenção possui informações sobre o tipo, status, custo, data de execução
 * e o fornecedor responsável pelo serviço.
 */
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

    /**
     * Ativo ao qual esta manutenção está vinculada.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    /**
     * Fornecedor responsável pela execução da manutenção.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_manutencao_id")
    private FornecedorManutencao fornecedor;

    /**
     * Data em que a manutenção foi realizada ou programada.
     */
    @PastOrPresent
    @Column(name = "data_manutencao", nullable = false)
    private LocalDate dataManutencao;

    /**
     * Tipo de manutenção: PREVENTIVA ou CORRETIVA.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoManutencao tipo;

    /**
     * Descrição do serviço realizado ou a realizar.
     */
    @Column(name = "descricao_servico", length = 255)
    private String descricaoServico;

    /**
     * Custo total da manutenção.
     */
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal custo;

    /**
     * Status atual da manutenção (ABERTA, EM_EXECUCAO, CONCLUIDA, CANCELADA etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusManutencao status;
}
