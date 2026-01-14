package br.com.unicos.ms_filial.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um parâmetro configurável de uma filial dentro do UniCoS.
 * <p>
 * No MVP do ms-filial, os parâmetros permitem armazenar configurações simples
 * por chave/valor (ex.: "USA_NFE"="true", "PRAZO_PADRAO_ENTREGA"="2").
 */
@Entity
@Table(
        name = "filial_parametro",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_filial_parametro_chave",
                        columnNames = {"filial_id", "chave"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FilialParametro extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da filial proprietária do parâmetro.
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Chave única do parâmetro por filial (ex.: "USA_NFE").
     */
    @NotBlank
    @Column(name = "chave", nullable = false, length = 80)
    private String chave;

    /**
     * Valor do parâmetro (armazenado como texto no MVP).
     */
    @NotBlank
    @Column(name = "valor", nullable = false, length = 500)
    private String valor;

    /**
     * Descrição do parâmetro para facilitar manutenção e leitura.
     */
    @Column(name = "descricao", length = 300)
    private String descricao;

    /**
     * Indica se o parâmetro está ativo para uso.
     */
    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
