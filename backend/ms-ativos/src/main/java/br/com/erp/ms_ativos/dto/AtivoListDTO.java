package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtivoListDTO {

    private Long id;
    private String nome;
    private String codigoPatrimonial;
    private TipoAtivo tipo;
    private StatusAtivo status;
}