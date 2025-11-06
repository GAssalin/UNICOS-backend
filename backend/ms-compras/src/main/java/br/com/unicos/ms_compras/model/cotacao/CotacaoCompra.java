package br.com.unicos.ms_compras.model.cotacao;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa a cotação de compra, vinculada a uma requisição ou pedido.
 *
 * <p>Contém informações gerais da cotação, como data de criação, prazo de validade
 * e os fornecedores participantes.</p>
 */
@Entity
@Table(name = "cotacao_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CotacaoCompra extends EntidadeAuditavel {

    /**
     * Código ou número identificador da cotação.
     */
    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    /**
     * Data de abertura da cotação.
     */
    @Column(nullable = false)
    private LocalDate dataAbertura;

    /**
     * Data limite de validade da cotação.
     */
    private LocalDate dataValidade;

    /**
     * Observações gerais da cotação.
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Fornecedores participantes desta cotação.
     */
    @OneToMany(mappedBy = "cotacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CotacaoFornecedor> fornecedores = new ArrayList<>();
}
