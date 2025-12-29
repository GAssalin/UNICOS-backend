package br.com.unicos.ms_empresa.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa o vínculo entre um usuário (ms-auth)
 * e uma empresa (tenant).
 */
@Entity
@Table(name = "empresa_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmpresaUsuario extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Empresa vinculada.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id_ref", nullable = false)
    private Empresa empresa;

    /**
     * Identificador do usuário no ms-auth.
     */
    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    /**
     * Perfil do usuário dentro da empresa.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PerfilEmpresaUsuario perfil;
}
