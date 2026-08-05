package br.com.unicos.ms_empresa.model;

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
 * Representa um parâmetro flexível no formato chave-valor
 * para customizações por empresa.
 */
@Entity
@Table(name = "empresa_parametro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmpresaParametro extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String chave;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String valor;
}
