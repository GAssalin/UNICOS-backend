package br.com.unicos.ms_cliente.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCategoriaRequestDTO {

    @NotBlank(message = "O nome da categoria é obrigatório.")
    private String nome;

    private String descricao;
}