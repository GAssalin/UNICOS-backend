package br.com.unicos.ms_empresa.model;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

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
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotBlank
    @Column(nullable = false, length = 2)
    private String uf;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String cep;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEnderecoEmpresa tipo;
}
