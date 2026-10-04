package br.com.unicos.ms_funcionario.dto.cargo;

import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CargoRequest(
    @NotBlank(message = "O nome do cargo é obrigatório.")
    @Size(max = 100, message = "O nome do cargo deve ter no máximo 100 caracteres.")
    String nome,
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    String descricao,
    @NotNull(message = "O papel do cargo é obrigatório.")
    PapelFuncionario papel
) { }
