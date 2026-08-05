package br.com.unicos.ms_empresa.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um endereço institucional da empresa.
 * <p>
 * Utilizado para fins fiscais, comerciais e administrativos.
 */
@Entity
@Table(name = "empresa_endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmpresaEndereco extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_endereco", nullable = false, length = 20)
    private TipoEnderecoEmpresa tipoEndereco;

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

    @NotBlank
    @Column(nullable = false, length = 120)
    private String municipio;

    @NotBlank
    @Column(nullable = false, length = 2)
    private String uf;

    @NotBlank
    @Column(nullable = false, length = 8)
    private String cep;

    @Column(nullable = false)
    private boolean principal;
}
