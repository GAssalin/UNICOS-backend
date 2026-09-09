package br.com.unicos.ms_cliente.dto.cliente_categoria;

import jakarta.validation.constraints.NotBlank;

public record ClienteCategoriaRequest(

    @NotBlank(message = "O nome da categoria é obrigatório.")
    String nome,

    String descricao
) { }
