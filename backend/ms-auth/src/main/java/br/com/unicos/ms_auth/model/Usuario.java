package br.com.unicos.ms_auth.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa cada usuário autenticável no sistema.
 * <p>
 * Contém apenas informações essenciais para autenticação e autorização.
 * Tokens temporários (como verificação de e-mail) são mantidos em entidades separadas.
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Usuario extends BaseTenantEntity implements UserDetails {

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

    @Column(name = "expiracao_refresh_token", length = 200)
    private LocalDateTime expiracaoRefreshToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_role",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public boolean isRefreshTokenExpirado() {
        return expiracaoRefreshToken == null || expiracaoRefreshToken.isBefore(LocalDateTime.now());
    }

}
