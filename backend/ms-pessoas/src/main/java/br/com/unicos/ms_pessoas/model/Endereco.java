package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um endereço associado a uma pessoa.
 * <p>
 * Estruturado para atender necessidades de cadastro público,
 * correspondência, logística e integrações corporativas.
 */
@Entity
@Table(name = "endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

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
     * Tipo do endereço (residencial, comercial etc.).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_endereco", nullable = false, length = 20)
    private TipoEndereco tipo;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String logradouro;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String bairro;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    @NotBlank
    @Column(nullable = false, length = 8)
    private String cep;

    /**
     * Indica se este é o endereço principal da pessoa.
     */
    @Column(name = "principal")
    private boolean principal;
}
