package br.com.unicos.ms_filial.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_filial.enums.StatusFilial;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa uma filial (unidade operacional) dentro do UniCoS.
 * <p>
 * É a entidade central do microserviço ms-filial, responsável por
 * identificar a unidade, vincular a empresa (tenant) proprietária
 * e manter o status operacional da filial.
 */
@Entity
@Table(
        name = "filial",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_filial_codigo", columnNames = {"codigo"}),
                @UniqueConstraint(name = "uk_filial_cnpj", columnNames = {"cnpj"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Filial extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno da filial (ex.: "001", "SBC-01").
     */
    @NotBlank
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Nome da filial/unidade (ex.: "Unidade Centro", "Loja Shopping X").
     */
    @NotBlank
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    /**
     * CNPJ da filial sem formatação.
     * <p>
     * Pode ser o mesmo da matriz em cenários específicos, mas no MVP
     * assume-se unicidade por cadastro.
     */
    @NotBlank
    @Column(name = "cnpj", nullable = false, length = 14)
    private String cnpj;

    /**
     * Status operacional da filial.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_filial", nullable = false, length = 20)
    private StatusFilial statusFilial;

    /**
     * Identificador da empresa (ms-empresa) proprietária desta filial.
     * Integração lógica, sem FK física.
     */
    @NotNull
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    /**
     * Identificador do endereço da filial (ms-filial).
     * Integração interna do microserviço, modelada por id para manter
     * o agregado mais simples no MVP.
     */
    @Column(name = "endereco_filial_id")
    private Long enderecoFilialId;

    /**
     * Identificador do contato principal da filial (ms-filial).
     * Integração interna do microserviço, modelada por id para manter
     * o agregado mais simples no MVP.
     */
    @Column(name = "contato_filial_id")
    private Long contatoFilialId;
}
