package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada de marcas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarcaListDTO {

    private Long id;
    private String nome;
}