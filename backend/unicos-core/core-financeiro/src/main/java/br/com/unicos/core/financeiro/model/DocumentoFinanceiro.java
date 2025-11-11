package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.TipoDocumentoFinanceiro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa um documento financeiro associado a um lançamento.
 * <p>
 * Pode representar notas fiscais, boletos, recibos ou outros comprovantes
 * utilizados para registro e controle das operações financeiras.
 */
@Entity
@Table(name = "documento_financeiro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoFinanceiro {

    /**
     * Identificador único do documento financeiro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo do documento (ex: nota fiscal, boleto, fatura).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoDocumentoFinanceiro tipoDocumento;

    /**
     * Número ou código identificador do documento.
     */
    @NotBlank
    @Column(nullable = false, length = 50, unique = true)
    private String numero;

    /**
     * Nome ou razão social do emissor do documento.
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String emissor;

    /**
     * Data de emissão do documento.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataEmissao;

    /**
     * Valor total do documento financeiro.
     */
    @NotNull
    @Column(nullable = false)
    private Double valorTotal;

    /**
     * Observações complementares sobre o documento.
     */
    @Column(length = 255)
    private String observacao;
}
