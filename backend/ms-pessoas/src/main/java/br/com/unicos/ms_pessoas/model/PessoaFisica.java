package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Representa uma pessoa física cadastrada no UniCoS.
 * <p>
 * Armazena CPF e informações complementares usadas em cadastros
 * corporativos, validações legais e integrações com outros módulos.
 */
@Entity
@Table(name = "pessoa_fisica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PessoaFisica extends Pessoa {

    /**
     * CPF da pessoa física sem formatação.
     */
    @NotBlank
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    /**
     * Data de nascimento da pessoa física.
     */
    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    /**
     * Nome social, quando aplicável.
     */
    @Column(name = "nome_social", length = 150)
    private String nomeSocial;
}
