package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Entidade que representa a unidade padrão de um produto,
 * podendo ser usada para conversões ou controle de embalagem.
 */
@Entity
@Table(name = "produto_unidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoUnidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto associado
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Unidade de medida associada (ex: unidade, kg, caixa)
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    /**
     * Quantidade padrão do produto nesta unidade
     */
    @NotNull
    @Positive(message = "A quantidade deve ser maior que zero.")
    @Column(name = "quantidade_padrao", nullable = false)
    private Double quantidadePadrao;

    /**
     * Fator de conversão entre unidades (ex: 1 caixa = 12 unidades)
     */
    @Positive
    @Column(name = "fator_conversao", nullable = false)
    @Builder.Default
    private Double fatorConversao = 1.0;
}
