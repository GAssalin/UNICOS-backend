package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um fornecedor no contexto do microserviço ms-compras.
 * <p>
 * Um fornecedor é a entidade base para processos de cotação, pedido de compra e recebimento.
 * Mantém dados cadastrais mínimos e pode possuir múltiplos contatos e endereços.
 */
@Entity
@Table(
        name = "fornecedor",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_fornecedor_codigo", columnNames = {"codigo"}),
                @UniqueConstraint(name = "uk_fornecedor_cnpj", columnNames = {"cnpj"})
        },
        indexes = {
                @Index(name = "ix_fornecedor_nome_fantasia", columnList = "nome_fantasia"),
                @Index(name = "ix_fornecedor_razao_social", columnList = "razao_social")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Fornecedor extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno do fornecedor (ex.: "FORN-001").
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Razão social do fornecedor.
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    /**
     * Nome fantasia do fornecedor (opcional).
     */
    @Size(max = 200)
    @Column(name = "nome_fantasia", length = 200)
    private String nomeFantasia;

    /**
     * CNPJ do fornecedor (somente dígitos ou formatado; validação específica pode ser feita via @CNPJ se você tiver).
     */
    @NotBlank
    @Size(max = 18)
    @Column(name = "cnpj", nullable = false, length = 18)
    private String cnpj;

    /**
     * Inscrição estadual (opcional).
     */
    @Size(max = 30)
    @Column(name = "inscricao_estadual", length = 30)
    private String inscricaoEstadual;

    /**
     * E-mail principal do fornecedor (opcional).
     */
    @Email
    @Size(max = 200)
    @Column(name = "email", length = 200)
    private String email;

    /**
     * Telefone principal do fornecedor (opcional).
     */
    @Size(max = 30)
    @Column(name = "telefone", length = 30)
    private String telefone;

    /**
     * Observações gerais do fornecedor (opcional).
     */
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

    /**
     * Lista de contatos do fornecedor.
     * <p>
     * Mantido como relacionamento para facilitar leitura/edição no cadastro.
     */
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContatoFornecedor> contatos = new ArrayList<>();

    /**
     * Lista de endereços do fornecedor.
     */
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EnderecoFornecedor> enderecos = new ArrayList<>();
}
