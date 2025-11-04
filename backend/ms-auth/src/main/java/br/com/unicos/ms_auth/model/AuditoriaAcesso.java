package br.com.unicos.ms_auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Registro de logins, falhas e eventos de autenticação.
 */
@Entity
@Table(name = "auditoria_acesso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 50)
    private String acao; // ex: LOGIN_SUCESSO, LOGIN_FALHA, LOGOUT

    @Column(length = 255)
    private String detalhes;

    @Column(nullable = false)
    private LocalDateTime dataEvento = LocalDateTime.now();

    @Column(length = 50)
    private String ip;
}
