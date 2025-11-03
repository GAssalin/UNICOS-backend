package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simples de categorias.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaListDTO {

    private Long id;
    private String nome;
}