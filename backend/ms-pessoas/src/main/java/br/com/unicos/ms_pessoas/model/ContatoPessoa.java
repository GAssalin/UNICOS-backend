package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoContato;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade que representa os meios de contato associados a uma pessoa.
 *
 * <p>Uma pessoa pode ter múltiplos contatos (telefone, celular, e-mail, etc.).</p>
 */
@Entity
@Table(name = "contato_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ContatoPessoa {

    /**
     * Identificador único do contato.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa proprietária do contato.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Tipo do contato (telefone, celular, e-mail, etc.).
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de contato é obrigatório.")
    @Column(name = "tipo_contato", nullable = false, length = 20)
    private TipoContato tipoContato;

    /**
     * Valor do contato (telefone, número ou e-mail).
     */
    @NotBlank(message = "O valor do contato é obrigatório.")
    @Size(max = 100, message = "O valor do contato deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String valor;

    /**
     * Descrição adicional (ex: telefone comercial, pessoal, recado, etc.).
     */
    @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres.")
    @Column(length = 100)
    private String descricao;

    /**
     * Indica se este é o contato principal da pessoa.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean principal = false;

    /**
     * Validação simples para padronizar o formato conforme o tipo de contato.
     *
     * <p>
     * Exemplo:
     * - Telefone/Celular: (11) 91234-5678
     * - E-mail: formato@dominio.com
     * </p>
     */
    @PrePersist
    @PreUpdate
    private void validarFormatoContato() {
        if (tipoContato == TipoContato.EMAIL && !valor.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de e-mail inválido: " + valor);
        }

        if ((tipoContato == TipoContato.TELEFONE || tipoContato == TipoContato.CELULAR || tipoContato == TipoContato.WHATSAPP)
                && !valor.matches("^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$")) {
            throw new IllegalArgumentException("Formato de telefone inválido: " + valor);
        }
    }
}
