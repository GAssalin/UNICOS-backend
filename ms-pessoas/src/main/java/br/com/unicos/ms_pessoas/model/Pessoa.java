package br.com.unicos.ms_pessoas.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa uma pessoa de forma genérica dentro do UniCoS.
 * <p>
 * Esta classe é a base para Pessoa Física e Pessoa Jurídica,
 * armazenando informações comuns como nome e tipo.
 * <p>
 * Utiliza estratégia de herança JOINED para garantir normalização,
 * mantendo tabelas separadas e coerentes com modelos corporativos.
 */
@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Pessoa extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome principal ou razão social simplificada.
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String nome;

    /**
     * Define se a pessoa é física ou jurídica.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPessoa tipoPessoa;
}
