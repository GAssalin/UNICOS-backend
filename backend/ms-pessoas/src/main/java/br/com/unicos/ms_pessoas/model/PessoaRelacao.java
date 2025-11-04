package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade que representa a relação entre uma pessoa e um tipo de vínculo.
 *
 * <p>Exemplo: Pessoa X possui a relação de Cliente, Fornecedor, ou Funcionário.</p>
 */
@Entity
@Table(name = "pessoa_relacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PessoaRelacao {

    /**
     * Identificador único da relação.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa à qual a relação pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Tipo de relação associado (Cliente, Fornecedor, Funcionário, etc.).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_relacao_id", nullable = false)
    @NotNull(message = "O tipo de relação é obrigatório.")
    private TipoRelacaoPessoa tipoRelacao;

    /**
     * Data de início da relação.
     */
    @Column(name = "data_inicio", nullable = false)
    @Builder.Default
    private LocalDate dataInicio = LocalDate.now();

    /**
     * Data de término da relação (caso aplicável).
     */
    @Column(name = "data_fim")
    private LocalDate dataFim;

    /**
     * Indica se a relação está ativa no momento.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;
}
