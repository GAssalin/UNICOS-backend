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
 * Entidade que representa uma pessoa jurídica.
 */
@Entity
@Table(name = "pessoa_juridica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PessoaJuridica extends Pessoa {

    /**
     * CNPJ da empresa.
     */
    @NotBlank(message = "O CNPJ é obrigatório.")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "O CNPJ deve estar no formato 00.000.000/0000-00.")
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    /**
     * Nome fantasia da empresa.
     */
    @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres.")
    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    /**
     * Inscrição estadual da empresa (opcional).
     */
    @Size(max = 30, message = "A inscrição estadual deve ter no máximo 30 caracteres.")
    @Column(name = "inscricao_estadual", length = 30)
    private String inscricaoEstadual;

    /**
     * Inscrição municipal da empresa (opcional).
     */
    @Size(max = 30, message = "A inscrição municipal deve ter no máximo 30 caracteres.")
    @Column(name = "inscricao_municipal", length = 30)
    private String inscricaoMunicipal;
}
