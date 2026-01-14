package br.com.unicos.ms_filial.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa os dados de contato de uma filial dentro do UniCoS.
 * <p>
 * No MVP do ms-filial, o contato é mantido como entidade separada e
 * pode ser referenciado por Filial via identificador.
 */
@Entity
@Table(name = "contato_filial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ContatoFilial extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da filial proprietária do contato.
     * Integração lógica, sem FK física (pode virar FK no futuro, se desejado).
     */
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Telefone principal (preferencialmente apenas dígitos no MVP).
     */
    @NotBlank
    @Column(name = "telefone_principal", nullable = false, length = 20)
    private String telefonePrincipal;

    /**
     * Telefone secundário (opcional).
     */
    @Column(name = "telefone_secundario", length = 20)
    private String telefoneSecundario;

    /**
     * E-mail principal de contato.
     */
    @Email
    @NotBlank
    @Column(name = "email_principal", nullable = false, length = 120)
    private String emailPrincipal;

    /**
     * E-mail secundário (opcional).
     */
    @Email
    @Column(name = "email_secundario", length = 120)
    private String emailSecundario;

    /**
     * Nome do responsável pelo contato (opcional no MVP).
     */
    @Column(name = "nome_responsavel", length = 120)
    private String nomeResponsavel;
}
