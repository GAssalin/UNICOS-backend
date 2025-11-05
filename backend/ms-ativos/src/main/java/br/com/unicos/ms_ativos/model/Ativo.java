package br.com.unicos.ms_ativos.model;

import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa um bem patrimonial registrado no imobilizado.
 * <p>
 * Cada ativo pode possuir documentos associados, histórico de manutenções,
 * depreciações mensais e movimentações (transferências entre unidades).
 */
@Entity
@Table(name = "ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome identificador do ativo (ex: Computador Dell Optiplex 7010).
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Código patrimonial único utilizado no controle interno.
     */
    @NotBlank
    @Column(nullable = false, unique = true, length = 30)
    private String codigoPatrimonial;

    /**
     * Descrição detalhada do ativo.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * Tipo de ativo (MÓVEL, IMÓVEL, VEÍCULO, EQUIPAMENTO etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAtivo tipo;

    /**
     * Status atual do ativo (ATIVO, INATIVO, EM_MANUTENCAO, BAIXADO etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAtivo status;

    /**
     * Data de aquisição do ativo.
     */
    @PastOrPresent
    @Column(name = "data_aquisicao", nullable = false)
    private LocalDate dataAquisicao;

    /**
     * Valor de aquisição original do ativo.
     */
    @DecimalMin("0.0")
    @Column(name = "valor_aquisicao", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorAquisicao;

    /**
     * Valor atual contabilizado (após depreciações e reavaliações).
     */
    @DecimalMin("0.0")
    @Column(name = "valor_atual", precision = 12, scale = 2)
    private BigDecimal valorAtual;

    /**
     * Identificador da empresa proprietária do ativo (referência ao ms-empresa).
     */
    @NotNull
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    /**
     * Identificador da filial onde o ativo está alocado (referência ao ms-empresa).
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Identificador do colaborador responsável (referência ao ms-pessoas).
     */
    @Column(name = "responsavel_id")
    private Long responsavelId;

    /**
     * Localização física do ativo (ex: Bloco A, Sala 204).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizacao_id")
    private Localizacao localizacao;

    // ===========================================================
    // 🔗 RELACIONAMENTOS
    // ===========================================================

    /**
     * Lista de documentos associados (notas fiscais, laudos, garantias).
     */
    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoAtivo> documentos;

    /**
     * Lista de manutenções realizadas ou programadas para o ativo.
     */
    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManutencaoAtivo> manutencoes;

    /**
     * Registros de depreciação mensal vinculados ao ativo.
     */
    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepreciacaoAtivo> depreciacoes;

    /**
     * Histórico consolidado das alterações de status e localização do ativo.
     */
    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoAtivo> historicos;

    /**
     * Registros de transferências do ativo entre unidades/filiais.
     */
    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferenciaAtivo> transferencias;
}
