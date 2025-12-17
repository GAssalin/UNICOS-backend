package br.com.unicos.ms_auth.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Registro de logins, falhas e eventos de autenticação.
 */
@Entity
@Table(name = "auditoria_acesso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuditoriaAcesso extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoAcaoAcesso acao;

    @Column(length = 255)
    private String detalhes;

    @Column(name = "data_evento", nullable = false)
    @Builder.Default
    private LocalDateTime dataEvento = LocalDateTime.now();

    @Column(length = 50)
    private String ip;

    @PrePersist
    public void prePersist() {
        this.dataEvento = LocalDateTime.now();
    }
}
