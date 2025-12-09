package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um documento pertencente a uma pessoa dentro do UniCoS.
 * <p>
 * Permite armazenar documentos como CPF, RG, CNPJ e outros,
 * possibilitando validações e integrações corporativas.
 */
@Entity
@Table(name = "documento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pessoa proprietária do documento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Tipo do documento (CPF, RG, CNPJ, etc.).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipo;

    /**
     * Número do documento, sem formatação.
     */
    @NotBlank
    @Column(nullable = false, length = 50)
    private String numero;

    /**
     * Órgão emissor, quando aplicável.
     */
    @Column(name = "orgao_emissor", length = 50)
    private String orgaoEmissor;

    /**
     * Data de emissão, quando disponível.
     */
    @Column(name = "data_emissao")
    private java.time.LocalDate dataEmissao;
}
