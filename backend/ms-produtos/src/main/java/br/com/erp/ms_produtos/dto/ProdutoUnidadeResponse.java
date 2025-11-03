package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado do vínculo entre produto e unidade de medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoUnidadeResponse {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Long unidadeMedidaId;
    private String unidadeMedidaNome;
    private Double quantidadePadrao;
}