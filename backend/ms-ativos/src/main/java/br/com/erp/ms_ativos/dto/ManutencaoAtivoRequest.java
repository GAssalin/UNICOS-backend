package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
public class ManutencaoAtivoRequest {

    @NotNull
    private Long ativoId;

    @NotNull
    @PastOrPresent
    private LocalDate dataManutencao;

    @NotNull
    private TipoManutencao tipo;

    @Size(max = 255)
    private String descricaoServico;

    @DecimalMin("0.0")
    private BigDecimal custo;

    @NotNull
    private StatusManutencao status;
}