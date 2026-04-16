package br.com.unicos.ms_empresa.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_empresa.enums.RegimeTributario;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa uma empresa (tenant) dentro do UniCoS.
 * <p>
 * É a entidade central do microserviço ms-empresa, responsável por
 * identificar, classificar e controlar o status operacional da empresa.
 */
@Entity
@Table(name = "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Empresa extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Razão social da empresa.
     */
    @NotBlank
    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    /**
     * Nome fantasia utilizado comercialmente.
     */
    @Column(name = "nome_fantasia", length = 200)
    private String nomeFantasia;

    /**
     * CNPJ da empresa sem formatação.
     */
    @NotBlank
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    /**
     * Tipo da empresa (MATRIZ ou FILIAL).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEmpresa tipoEmpresa;

    /**
     * Status operacional da empresa.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEmpresa statusEmpresa;

    /**
     * Regime tributário da empresa.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RegimeTributario regimeTributario;

    /**
     * Data de abertura da empresa.
     */
    @Column(name = "data_abertura")
    private LocalDate dataAbertura;

    /**
     * Identificador da pessoa jurídica no ms-pessoas.
     * Integração lógica, sem FK física.
     */
    @Column(name = "pessoa_juridica_id")
    private Long pessoaJuridicaId;
}
