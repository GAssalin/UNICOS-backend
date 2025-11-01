package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtivoRequest {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 30)
    private String codigoPatrimonial;

    @Size(max = 255)
    private String descricao;

    @NotNull
    private TipoAtivo tipo;

    @NotNull
    private StatusAtivo status;

    @NotNull
    @PastOrPresent
    private LocalDate dataAquisicao;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal valorAquisicao;

    @DecimalMin("0.0")
    private BigDecimal valorAtual;

    @NotNull
    private Long empresaId;

    @NotNull
    private Long filialId;

    private Long responsavelId;

    private Long localizacaoId;
}