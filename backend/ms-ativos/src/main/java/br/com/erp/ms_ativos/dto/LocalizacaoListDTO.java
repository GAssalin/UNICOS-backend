package br.com.erp.ms_ativos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizacaoListDTO {

    private Long id;
    private String descricao;
    private String andar;
}