package br.com.unicos.ms_pessoas.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_pessoas.enums.TipoContato;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa um meio de contato associado a uma pessoa.
 * <p>
 * Podem ser utilizados telefones, celulares e e-mails para comunicação
 * operacional e integração com outros módulos, como notificações e agenda.
 */
@Entity
@Table(name = "contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contato extends BaseTenantEntity {

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
