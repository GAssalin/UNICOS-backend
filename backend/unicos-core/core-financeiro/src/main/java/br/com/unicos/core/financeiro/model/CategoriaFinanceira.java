package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.TipoLancamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma categoria financeira.
 * <p>
 * Utilizada para classificar receitas e despesas de forma analítica,
 * facilitando a geração de relatórios e o controle gerencial.
 */
@Entity
@Table(name = "categoria_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaFinanceira {

    /**
     * Identificador único da categoria financeira.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da categoria (ex: Vendas, Salários, Tarifas Bancárias).
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Tipo da categoria (receita ou despesa).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoLancamento tipo;

    /**
     * Indica se a categoria está ativa para uso.
     */
    @Column(nullable = false)
    private Boolean ativo;
}
