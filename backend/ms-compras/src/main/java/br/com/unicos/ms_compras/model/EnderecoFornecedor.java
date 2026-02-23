package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa um endereço vinculado a um fornecedor no contexto do ms-compras.
 * <p>
 * Um fornecedor pode possuir múltiplos endereços (ex.: matriz, filial, cobrança, entrega).
 */
@Entity
@Table(
        name = "endereco_fornecedor",
        indexes = {
                @Index(name = "ix_endereco_fornecedor_fornecedor_id", columnList = "fornecedor_id"),
                @Index(name = "ix_endereco_fornecedor_cep", columnList = "cep")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class EnderecoFornecedor extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fornecedor dono do endereço.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_endereco_fornecedor_fornecedor"))
    private Fornecedor fornecedor;

    /**
     * Descrição/tipo do endereço (ex.: "ENTREGA", "COBRANÇA", "MATRIZ", "FILIAL").
     * <p>
     * Mantido como texto no MVP; se preferir, podemos converter para enum depois.
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    /**
     * CEP do endereço.
     */
    @NotBlank
    @Size(max = 10)
    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    /**
     * Logradouro (rua/avenida).
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "logradouro", nullable = false, length = 200)
    private String logradouro;

    /**
     * Número do endereço.
     */
    @NotBlank
    @Size(max = 20)
    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    /**
     * Complemento do endereço (opcional).
     */
    @Size(max = 100)
    @Column(name = "complemento", length = 100)
    private String complemento;

    /**
     * Bairro do endereço.
     */
    @NotBlank
    @Size(max = 120)
    @Column(name = "bairro", nullable = false, length = 120)
    private String bairro;

    /**
     * Cidade do endereço.
     */
    @NotBlank
    @Size(max = 120)
    @Column(name = "cidade", nullable = false, length = 120)
    private String cidade;

    /**
     * UF do endereço (ex.: "SP").
     */
    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    /**
     * Observação do endereço (opcional).
     */
    @Size(max = 300)
    @Column(name = "observacao", length = 300)
    private String observacao;
}
