package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simples de vínculos produto–unidade de medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoUnidadeListDTO {

    private Long id;
    private String produtoNome;
    private String unidadeMedidaNome;
    private Double quantidadePadrao;
}