package br.com.unicos.ms_empresa.model;

import br.com.unicos.ms_empresa.enums.TipoAmbiente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entidade responsável por armazenar as configurações fiscais da empresa.
 * Inclui informações sobre o ambiente fiscal (produção ou homologação),
 * regime tributário e certificado digital.
 */
@Entity
@Table(name = "configuracao_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(length = 255)
    private String regimeTributario;

    @Column(length = 255)
    private String certificadoDigital;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAmbiente tipoAmbiente;
}
