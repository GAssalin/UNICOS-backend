package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO usado para listagem simplificada de fornecedores vinculados a produtos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorProdutoListDTO {

    private Long id;
    private Long fornecedorId;
    private String produtoNome;
    private BigDecimal precoCusto;
}