package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManutencaoAtivoListDTO {

    private Long id;
    private Long ativoId;
    private LocalDate dataManutencao;
    private TipoManutencao tipo;
    private StatusManutencao status;
}