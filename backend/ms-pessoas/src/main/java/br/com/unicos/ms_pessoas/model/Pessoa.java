package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade base que representa uma pessoa genérica
 * (física ou jurídica).
 *
 * <p>As subclasses {@link PessoaFisica} e {@link PessoaJuridica}
 * especializam esta classe conforme o tipo de pessoa.</p>
 */
@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pessoa {

    /**
     * Identificador único da pessoa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo ou razão social da pessoa.
     */
    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    @Column(nullable = false, length = 150)
    private String nome;

    /**
     * Tipo da pessoa (física ou jurídica).
     */
    @NotNull(message = "O tipo de pessoa é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false, length = 10)
    private TipoPessoa tipoPessoa;

    /**
     * Indica se o registro está ativo.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    /**
     * Data e hora em que o registro foi criado.
     */
    @CreationTimestamp
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    /**
     * Data e hora da última atualização do registro.
     */
    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;
}
