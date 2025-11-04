package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade que representa o vínculo de uma pessoa física com a empresa.
 *
 * <p>Permite definir cargo, departamento e informações contratuais do colaborador.</p>
 */
@Entity
@Table(name = "colaborador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Colaborador {

    /**
     * Identificador único do colaborador.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa associada ao colaborador (deve ser uma Pessoa Física).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    @NotNull(message = "A pessoa é obrigatória.")
    private PessoaFisica pessoa;

    /**
     * Identificador da empresa a qual o colaborador pertence.
     *
     * <p>Este campo referencia a entidade Empresa no ms-empresa (integração via ID).</p>
     */
    @NotNull(message = "O ID da empresa é obrigatório.")
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    /**
     * Cargo do colaborador.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    /**
     * Identificador do departamento ao qual o colaborador pertence.
     *
     * <p>Este campo referencia a entidade Departamento no ms-empresa (integração via ID).</p>
     */
    @Column(name = "departamento_id", nullable = false)
    private Long departamentoId;

    /**
     * Data de admissão do colaborador.
     */
    @Column(name = "data_admissao", nullable = false)
    @Builder.Default
    private LocalDate dataAdmissao = LocalDate.now();

    /**
     * Data de desligamento (caso aplicável).
     */
    @Column(name = "data_desligamento")
    private LocalDate dataDesligamento;

    /**
     * Matrícula interna do colaborador.
     */
    @Column(length = 30, unique = true)
    private String matricula;

    /**
     * Indica se o colaborador está ativo.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;
}
