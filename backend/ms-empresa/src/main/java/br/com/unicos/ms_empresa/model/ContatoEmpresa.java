package br.com.unicos.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Armazena informações de contato da empresa, como telefone e e-mail.
 * <p>
 * Pode ser expandida futuramente para suportar múltiplos tipos de
 * comunicação (comercial, fiscal, suporte, etc.).
 */
@Entity
@Table(name = "contato_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String telefone;

    @Email
    @Column(length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
}
