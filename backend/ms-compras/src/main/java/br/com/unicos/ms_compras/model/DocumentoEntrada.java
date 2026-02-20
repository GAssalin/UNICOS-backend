package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa um documento de entrada vinculado ao recebimento da compra.
 * <p>
 * Pode ser nota fiscal, recibo, conhecimento de transporte, etc.
 * O armazenamento do arquivo (PDF/imagem) normalmente fica fora do banco (ex.: storage),
 * e aqui guardamos apenas metadados e uma referência (URL/chave).
 */
@Entity
@Table(
        name = "documento_entrada",
        indexes = {
                @Index(name = "ix_documento_entrada_recebimento_id", columnList = "recebimento_compra_id"),
                @Index(name = "ix_documento_entrada_tipo", columnList = "tipo_documento"),
                @Index(name = "ix_documento_entrada_numero", columnList = "numero")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class DocumentoEntrada extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Recebimento ao qual este documento pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recebimento_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_documento_entrada_recebimento"))
    private RecebimentoCompra recebimentoCompra;

    /**
     * Tipo do documento (MVP texto: "NF", "RECIBO", "CTE"...).
     * <p>
     * Você pode trocar para enum TipoDocumentoEntrada com @Enumerated.
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "tipo_documento", nullable = false, length = 30)
    private String tipoDocumento;

    /**
     * Número do documento.
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "numero", nullable = false, length = 30)
    private String numero;

    /**
     * Série do documento (opcional).
     */
    @Size(max = 10)
    @Column(name = "serie", length = 10)
    private String serie;

    /**
     * Chave de acesso (NF-e), se aplicável (opcional).
     */
    @Size(max = 60)
    @Column(name = "chave_acesso", length = 60)
    private String chaveAcesso;

    /**
     * Data de emissão do documento (opcional).
     */
    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    /**
     * Referência para arquivo do documento (URL/chave no storage), opcional.
     */
    @Size(max = 500)
    @Column(name = "arquivo_ref", length = 500)
    private String arquivoRef;

    /**
     * Observações do documento (opcional).
     */
    @Size(max = 300)
    @Column(name = "observacao", length = 300)
    private String observacao;
}
