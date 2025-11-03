package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simples de unidades de medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeMedidaListDTO {

    private Long id;
    private String nome;
    private String sigla;
}