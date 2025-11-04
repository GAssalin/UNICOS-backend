package br.com.unicos.ms_auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Armazena tokens JWT válidos/emitidos para controle de sessão e revogação.
 */
@Entity
@Table(name = "token_acesso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime dataEmissao;

    private LocalDateTime dataExpiracao;

    @Builder.Default
    @Column(nullable = false)
    private boolean valido = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
