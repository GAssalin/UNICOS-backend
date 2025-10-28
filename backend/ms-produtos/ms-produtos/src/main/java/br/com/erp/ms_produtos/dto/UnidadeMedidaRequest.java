package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para criação e atualização de unidades de medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeMedidaRequest {

    @NotBlank(message = "O nome da unidade é obrigatório.")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
    private String nome;

    @NotBlank(message = "A sigla é obrigatória.")
    @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres.")
    private String sigla;
}