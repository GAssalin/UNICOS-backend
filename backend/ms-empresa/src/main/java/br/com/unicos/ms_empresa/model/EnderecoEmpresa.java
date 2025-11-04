package br.com.unicos.ms_empresa.model;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Armazena os endereços associados a uma empresa ou filial.
 * <p>
 * Permite classificar endereços conforme sua finalidade
 * (por exemplo, matriz, entrega, cobrança, faturamento, etc.)
 * por meio da enum {@link TipoEnderecoEmpresa}.
 */
@Entity
@Table(name = "endereco_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    @NotBlank
    private String cep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEnderecoEmpresa tipoEndereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;
}
