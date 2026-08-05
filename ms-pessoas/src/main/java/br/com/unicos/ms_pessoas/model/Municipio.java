package br.com.unicos.ms_pessoas.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_pessoas.enums.Uf;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa um município brasileiro, associado a um endereço.
 * <p>
 * Ideal para normalização, padronização de cadastros e integrações
 * que utilizam informações geográficas e códigos IBGE.
 */
@Entity
@Table(name = "municipio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Municipio extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do município.
     */
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    /**
     * Unidade Federativa (UF) à qual o município pertence.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Uf uf;

    /**
     * Código IBGE do município.
     */
    @Column(name = "codigo_ibge", length = 10)
    private String codigoIbge;
}
