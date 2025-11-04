package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade que representa o cargo ocupado por um colaborador.
 *
 * <p>Exemplo: Analista, Gerente, Supervisor, Diretor.</p>
 */
@Entity
@Table(name = "cargo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Cargo {

    /**
     * Identificador único do cargo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do cargo.
     */
    @NotBlank(message = "O nome do cargo é obrigatório.")
    @Size(max = 100, message = "O nome do cargo deve ter no máximo 100 caracteres.")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    /**
     * Descrição opcional sobre o cargo.
     */
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Column(length = 255)
    private String descricao;

    /**
     * Indica se o cargo está ativo.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;
}
