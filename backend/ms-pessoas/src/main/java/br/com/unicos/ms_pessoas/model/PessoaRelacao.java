package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Representa o vínculo existente entre duas pessoas cadastradas no UniCoS.
 * <p>
 * Cada registro indica uma relação entre uma pessoa principal e outra pessoa relacionada,
 * classificada por um tipo previamente definido em {@link TipoRelacaoPessoa}.
 * <p>
 * Exemplos de relações: Pai, Mãe, Dependente, Sócio, Representante Legal,
 * responsável financeiro, tutor, entre outros.
 */
@Entity
@Table(name = "pessoa_relacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PessoaRelacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa principal do relacionamento.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Pessoa que mantém a relação com a pessoa principal.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relacionado_id", nullable = false)
    private Pessoa relacionado;

    /**
     * Tipo da relação estabelecida entre as pessoas.
     * Exemplo: Pai, Mãe, Sócio, Dependente, Representante Legal.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_relacao_pessoa_id", nullable = false)
    private TipoRelacaoPessoa tipoRelacao;
}
