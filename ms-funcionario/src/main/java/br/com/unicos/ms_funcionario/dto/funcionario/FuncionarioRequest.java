package br.com.unicos.ms_funcionario.dto.funcionario;

import br.com.unicos.ms_funcionario.enums.StatusFuncionario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record FuncionarioRequest(
    @NotNull(message = "O identificador da pessoa é obrigatório.")
    Long pessoaId,
    Long usuarioId,
    @Size(max = 30, message = "A matrícula deve ter no máximo 30 caracteres.")
    String matricula,
    @NotNull(message = "O cargo é obrigatório.")
    Long cargoId,
    Long superiorId,
    Long filialId,
    @NotNull(message = "A data de admissão é obrigatória.")
    LocalDate dataAdmissao,
    LocalDate dataDesligamento,
    @NotNull(message = "O status do funcionário é obrigatório.")
    StatusFuncionario status
) { }
