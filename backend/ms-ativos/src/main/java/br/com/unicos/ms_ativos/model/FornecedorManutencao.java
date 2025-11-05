package br.com.unicos.ms_ativos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidade que representa fornecedores ou prestadores de serviço de manutenção.
 */
@Entity
@Table(name = "fornecedor_manutencao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome ou razão social do fornecedor.
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String nome;

    /**
     * CNPJ do fornecedor.
     */
    @NotBlank
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    /**
     * Telefone de contato do fornecedor.
     */
    @Column(length = 20)
    private String telefone;

    /**
     * E-mail de contato principal.
     */
    @Email
    @Column(length = 150)
    private String email;

    /**
     * Nome do responsável técnico ou comercial.
     */
    @Column(length = 100)
    private String responsavel;

    /**
     * Lista de manutenções associadas a este fornecedor.
     */
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<ManutencaoAtivo> manutencoes;
}
