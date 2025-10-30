package br.com.erp.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuracao_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoEmpresa {

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
