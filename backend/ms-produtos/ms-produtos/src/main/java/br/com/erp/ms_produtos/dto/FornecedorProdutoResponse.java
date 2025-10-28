package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO usado para retorno detalhado de FornecedorProduto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorProdutoResponse {

    private Long id;
    private Long fornecedorId;
    private Long produtoId;
    private String produtoNome;
    private BigDecimal precoCusto;
    private Integer prazoEntregaDias;
}