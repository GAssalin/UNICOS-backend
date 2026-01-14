package br.com.unicos.ms_filial.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa o endereço de uma filial dentro do UniCoS.
 * <p>
 * No MVP do ms-filial, o endereço é modelado como entidade separada e
 * pode ser referenciado por Filial via identificador (sem composição direta),
 * mantendo o agregado de Filial mais simples.
 */
@Entity
@Table(name = "endereco_filial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EnderecoFilial extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da filial proprietária do endereço.
     * Integração lógica, sem FK física (pode virar FK no futuro, se desejado).
     */
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Logradouro (ex.: Rua, Avenida) e nome (ex.: "Avenida Paulista").
     */
    @NotBlank
    @Column(name = "logradouro", nullable = false, length = 200)
    private String logradouro;

    /**
     * Número do endereço.
     */
    @NotBlank
    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    /**
     * Complemento (ex.: "Sala 12", "Bloco B").
     */
    @Column(name = "complemento", length = 100)
    private String complemento;

    /**
     * Bairro do endereço.
     */
    @NotBlank
    @Column(name = "bairro", nullable = false, length = 120)
    private String bairro;

    /**
     * Cidade do endereço.
     */
    @NotBlank
    @Column(name = "cidade", nullable = false, length = 120)
    private String cidade;

    /**
     * UF (estado) do endereço (ex.: "SP").
     */
    @NotBlank
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    /**
     * CEP sem formatação.
     */
    @NotBlank
    @Column(name = "cep", nullable = false, length = 8)
    private String cep;
}
