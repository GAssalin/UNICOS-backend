package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado de unidade de medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeMedidaResponse {

    private Long id;
    private String nome;
    private String sigla;
}