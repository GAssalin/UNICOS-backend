package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa uma pessoa de contato vinculada a um fornecedor.
 * <p>
 * Permite registrar responsáveis por áreas específicas como
 * compras, faturamento, logística ou comercial.
 */
@Entity
@Table(
        name = "contato_fornecedor",
        indexes = {
                @Index(name = "ix_contato_fornecedor_fornecedor_id", columnList = "fornecedor_id"),
                @Index(name = "ix_contato_fornecedor_nome", columnList = "nome")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ContatoFornecedor extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fornecedor ao qual o contato pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_contato_fornecedor_fornecedor"))
    private Fornecedor fornecedor;

    /**
     * Nome do contato.
     */
    @NotBlank
    @Size(max = 150)
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    /**
     * Cargo ou setor do contato (ex.: "Compras", "Financeiro", "Vendas").
     */
    @Size(max = 100)
    @Column(name = "cargo", length = 100)
    private String cargo;

    /**
     * Telefone principal do contato.
     */
    @Size(max = 30)
    @Column(name = "telefone", length = 30)
    private String telefone;

    /**
     * Telefone celular/whatsapp.
     */
    @Size(max = 30)
    @Column(name = "celular", length = 30)
    private String celular;

    /**
     * E-mail do contato.
     */
    @Email
    @Size(max = 200)
    @Column(name = "email", length = 200)
    private String email;

    /**
     * Indica se é o contato principal do fornecedor.
     */
    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    /**
     * Observações adicionais.
     */
    @Size(max = 400)
    @Column(name = "observacao", length = 400)
    private String observacao;
}
