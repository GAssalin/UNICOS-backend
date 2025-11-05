package br.com.unicos.ms_ativos.model;

import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa documentos vinculados a um ativo,
 * como notas fiscais, garantias, laudos técnicos e certificados.
 */
@Entity
@Table(name = "documento_ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ativo ao qual o documento pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    /**
     * Tipo do documento (nota fiscal, garantia, laudo, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoDocumentoAtivo tipo;

    /**
     * Número de identificação ou referência do documento.
     */
    @NotBlank
    @Column(name = "numero", nullable = false, length = 50)
    private String numero;

    /**
     * Data de emissão do documento.
     */
    @PastOrPresent
    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    /**
     * Caminho ou URL do arquivo digitalizado.
     */
    @Column(name = "arquivo_url", length = 255)
    private String arquivoUrl;

    /**
     * Status atual do documento (VÁLIDO, EXPIRADO, VENCIDO, OUTRO).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusDocumento status;

    /**
     * Observações gerais sobre o documento.
     */
    @Column(length = 255)
    private String observacao;
}
