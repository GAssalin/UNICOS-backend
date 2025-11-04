package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.enums.TipoLogradouro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade que representa o endereço associado a uma pessoa.
 */
@Entity
@Table(name = "endereco_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class EnderecoPessoa {

    /**
     * Identificador único do endereço.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa proprietária do endereço.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Tipo do endereço (residencial, comercial, cobrança, entrega).
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de endereço é obrigatório.")
    @Column(name = "tipo_endereco", nullable = false, length = 20)
    private TipoEndereco tipoEndereco;

    /**
     * Tipo do logradouro (rua, avenida, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_logradouro", length = 20)
    private TipoLogradouro tipoLogradouro;

    /**
     * Nome do logradouro (ex: Rua das Flores).
     */
    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 120, message = "O logradouro deve ter no máximo 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String logradouro;

    /**
     * Número do imóvel.
     */
    @NotBlank(message = "O número é obrigatório.")
    @Size(max = 10, message = "O número deve ter no máximo 10 caracteres.")
    @Column(nullable = false, length = 10)
    private String numero;

    /**
     * Complemento (apartamento, bloco, etc.).
     */
    @Size(max = 60, message = "O complemento deve ter no máximo 60 caracteres.")
    @Column(length = 60)
    private String complemento;

    /**
     * Bairro do endereço.
     */
    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 60, message = "O bairro deve ter no máximo 60 caracteres.")
    @Column(nullable = false, length = 60)
    private String bairro;

    /**
     * CEP no formato 00000-000.
     */
    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 00000-000.")
    @Column(nullable = false, length = 9)
    private String cep;

    /**
     * Município (cidade) associado ao endereço.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    /**
     * Indica se este é o endereço principal da pessoa.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean principal = false;
}
