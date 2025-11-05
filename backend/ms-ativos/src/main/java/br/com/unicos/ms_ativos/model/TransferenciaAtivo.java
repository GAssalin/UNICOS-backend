package br.com.unicos.ms_ativos.model;

import br.com.unicos.ms_ativos.enums.TipoTransferencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa a movimentação (transferência) de um ativo
 * entre unidades, filiais ou setores.
 * <p>
 * As transferências permitem rastrear a localização e o histórico
 * de movimentação de cada ativo ao longo de seu ciclo de vida.
 */
@Entity
@Table(name = "transferencia_ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ativo que está sendo transferido.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    /**
     * Identificador da filial/unidade de origem (referência ao ms-empresa).
     */
    @NotNull
    @Column(name = "origem_id", nullable = false)
    private Long origemId;

    /**
     * Identificador da filial/unidade de destino (referência ao ms-empresa).
     */
    @NotNull
    @Column(name = "destino_id", nullable = false)
    private Long destinoId;

    /**
     * Tipo de transferência (INTERNA, ENTRE_FILIAIS, BAIXA, OUTROS).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoTransferencia tipo;

    /**
     * Data em que a transferência foi realizada ou registrada.
     */
    @PastOrPresent
    @Column(name = "data_transferencia", nullable = false)
    private LocalDate dataTransferencia;

    /**
     * Usuário responsável pela transferência (referência ao ms-pessoas).
     */
    @Column(name = "responsavel_id")
    private Long responsavelId;

    /**
     * Motivo ou observações da transferência.
     */
    @Column(length = 255)
    private String motivo;
}
