package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado de marca.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarcaResponse {

    private Long id;
    private String nome;
    private Integer quantidadeProdutos;
}