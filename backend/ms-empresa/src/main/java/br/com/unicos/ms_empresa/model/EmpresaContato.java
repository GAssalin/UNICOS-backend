package br.com.unicos.ms_empresa.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um canal de contato institucional da empresa.
 */
@Entity
@Table(name = "empresa_contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmpresaContato extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Empresa proprietária do contato.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id_ref", nullable = false)
    private Empresa empresa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contato", nullable = false, length = 20)
    private TipoContatoEmpresa tipoContato;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String valor;

    @Column(nullable = false)
    private boolean principal;
}
