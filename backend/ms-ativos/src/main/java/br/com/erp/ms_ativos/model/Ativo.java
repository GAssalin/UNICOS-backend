package br.com.erp.ms_ativos.model;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 30)
    private String codigoPatrimonial;

    @Column(length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAtivo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAtivo status;

    @PastOrPresent
    @Column(name = "data_aquisicao", nullable = false)
    private LocalDate dataAquisicao;

    @DecimalMin("0.0")
    @Column(name = "valor_aquisicao", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorAquisicao;

    @DecimalMin("0.0")
    @Column(name = "valor_atual", precision = 12, scale = 2)
    private BigDecimal valorAtual;

    @NotNull
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    @Column(name = "responsavel_id")
    private Long responsavelId;

    @ManyToOne
    @JoinColumn(name = "localizacao_id")
    private Localizacao localizacao;
}