package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoContato;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um meio de contato associado a uma pessoa.
 * <p>
 * Podem ser utilizados telefones, celulares e e-mails para comunicação
 * operacional e integração com outros módulos, como notificações e agenda.
 */
@Entity
@Table(name = "contato")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa dona do contato.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Tipo do contato (telefone, celular, e-mail).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contato", nullable = false, length = 20)
    private TipoContato tipo;

    /**
     * Valor do contato (número ou e-mail).
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String valor;

    /**
     * Indica se este é o contato principal da pessoa.
     */
    @Column(name = "principal")
    private boolean principal;
}
