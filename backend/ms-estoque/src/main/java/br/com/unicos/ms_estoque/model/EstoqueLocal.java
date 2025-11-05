package br.com.unicos.ms_estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(length = 50)
    private String tipo; // Ex: DEPOSITO, LOJA, TERCEIRO

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId; // Referência ao ms-empresa
}
