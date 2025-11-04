package br.com.unicos.ms_pessoas.model;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade que representa um documento associado a uma pessoa.
 *
 * <p>Permite armazenar múltiplos documentos por pessoa,
 * como CPF, RG, CNH, CNPJ, entre outros.</p>
 */
@Entity
@Table(name = "documento_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class DocumentoPessoa {

    /**
     * Identificador único do documento.
     */
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
     * Tipo do documento (CPF, RG, CNPJ, CNH, etc.).
     */
    @NotNull(message = "O tipo de documento é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 30)
    private TipoDocumento tipoDocumento;

    /**
     * Número do documento.
     */
    @NotBlank(message = "O número do documento é obrigatório.")
    @Size(max = 30, message = "O número do documento deve ter no máximo 30 caracteres.")
    @Column(nullable = false, length = 30)
    private String numero;

    /**
     * Órgão emissor do documento (ex: SSP-SP, DETRAN, Receita Federal).
     */
    @Size(max = 30, message = "O órgão emissor deve ter no máximo 30 caracteres.")
    @Column(name = "orgao_emissor", length = 30)
    private String orgaoEmissor;

    /**
     * UF de emissão do documento.
     */
    @Size(max = 2, message = "A UF deve ter no máximo 2 caracteres.")
    @Column(name = "uf_emissor", length = 2)
    private String ufEmissor;

    /**
     * Data de emissão do documento.
     */
    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    /**
     * Data de validade do documento (para CNH, passaportes, etc.).
     */
    @Column(name = "data_validade")
    private LocalDate dataValidade;

    /**
     * Identificador do arquivo vinculado (upload ou storage externo).
     * Pode ser utilizado para integração com ms-documentos.
     */
    @Size(max = 255)
    @Column(name = "arquivo_id", length = 255)
    private String arquivoId;

    /**
     * Indica se o documento está ativo.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;
}
