package br.com.unicos.ms_estoque.model;

import br.com.unicos.ms_estoque.enums.TipoAjusteEstoque;
import br.com.unicos.ms_estoque.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa uma transação de movimentação no estoque.
 * Pode ser de entrada, saída, transferência ou ajuste.
 */
@Entity
@Table(name = "transacao_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo principal da transação de estoque.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TipoTransacao tipo;

    /**
     * Tipo de ajuste aplicado (quando a transação for do tipo AJUSTE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ajuste", length = 30)
    private TipoAjusteEstoque tipoAjuste;

    /**
     * Observações gerais sobre a transação.
     */
    @Column(length = 255)
    private String observacao;

    /**
     * Data e hora de execução da transação.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime data = LocalDateTime.now();

    /**
     * Usuário responsável pela movimentação.
     */
    @Column(name = "usuario_responsavel", length = 100)
    private String usuarioResponsavel;
}
