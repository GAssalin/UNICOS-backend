package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidade que representa uma pessoa física.
 */
@Entity
@Table(name = "pessoa_fisica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PessoaFisica extends Pessoa {

    /**
     * CPF da pessoa física.
     */
    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "O CPF deve estar no formato 000.000.000-00.")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    /**
     * RG ou documento de identificação.
     */
    @Size(max = 20, message = "O RG deve ter no máximo 20 caracteres.")
    @Column(length = 20)
    private String rg;

    /**
     * Data de nascimento no formato yyyy-MM-dd.
     */
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "A data de nascimento deve estar no formato yyyy-MM-dd.")
    @Column(name = "data_nascimento", length = 10)
    private String dataNascimento;

    /**
     * Gênero ou sexo (opcional).
     */
    @Size(max = 20, message = "O gênero deve ter no máximo 20 caracteres.")
    @Column(length = 20)
    private String genero;
}
