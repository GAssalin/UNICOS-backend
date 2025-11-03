package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado de atributos personalizados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtributoPersonalizadoResponse {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String nome;
    private String valor;
}