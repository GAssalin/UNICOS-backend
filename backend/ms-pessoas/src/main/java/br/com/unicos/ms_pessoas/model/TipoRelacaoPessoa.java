package br.com.unicos.ms_pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade que define os tipos de relação que uma pessoa pode possuir.
 *
 * <p>Exemplos: Cliente, Fornecedor, Funcionário, Representante, etc.</p>
 */
@Entity
@Table(name = "tipo_relacao_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class TipoRelacaoPessoa {

    /**
     * Identificador único do tipo de relação.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno do tipo de relação (ex: CLIENTE, FORNECEDOR).
     */
    @NotBlank(message = "O código é obrigatório.")
    @Size(max = 30, message = "O código deve ter no máximo 30 caracteres.")
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    /**
     * Descrição do tipo de relação (ex: Cliente, Fornecedor, Funcionário).
     */
    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String descricao;

    /**
     * Indica se este tipo de relação está ativo.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;
}
