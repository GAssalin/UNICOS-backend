package br.com.unicos.ms_usuario.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Representa um usuário autenticável do sistema.
 *
 * <p>
 * Este microserviço é responsável apenas pelos dados cadastrais
 * do usuário. Informações de autenticação, roles e permissões
 * são gerenciadas exclusivamente pelo ms-auth.
 * </p>
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Usuario extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 100)
    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "pessoa_id")
    private Long pessoaId;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Email
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @Builder.Default
    @Column(name = "email_verificado", nullable = false)
    private boolean emailVerificado = false;

    @Column(name = "refresh_token", length = 200)
    private String refreshToken;

    @Column(name = "expiracao_refresh_token")
    private LocalDateTime expiracaoRefreshToken;

    /**
     * Verifica se o refresh token está expirado.
     */
    public boolean isRefreshTokenExpirado() {
        return expiracaoRefreshToken == null || expiracaoRefreshToken.isBefore(LocalDateTime.now());
    }
}
