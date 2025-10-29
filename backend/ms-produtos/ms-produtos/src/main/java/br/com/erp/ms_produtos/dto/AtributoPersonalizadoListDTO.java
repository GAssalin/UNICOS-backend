package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada de atributos personalizados de produtos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtributoPersonalizadoListDTO {

    private Long id;
    private String nome;
    private String valor;
    private String produtoNome;
}