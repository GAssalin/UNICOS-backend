package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para criação e atualização de categorias.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres.")
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String descricao;
}