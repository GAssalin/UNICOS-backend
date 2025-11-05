package br.com.unicos.ms_estoque.model;

import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Representa um local físico ou lógico de armazenamento de produtos.
 * Exemplo: "Depósito Central", "Loja São Paulo".
 */
@Entity
@Table(name = "estoque_local")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstoqueLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome identificador do local de estoque.
     */
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    /**
     * Descrição adicional sobre o local de estoque.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * Tipo do local de estoque (ex: depósito, loja, terceiro, produção).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoLocalEstoque tipo;

    /**
     * ID da empresa ou filial associada ao local.
     * Este valor referencia o ms-empresa.
     */
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;
}
