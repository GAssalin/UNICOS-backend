package br.com.unicos.ms_funcionario.dto.cargo;

import br.com.unicos.ms_funcionario.enums.PapelFuncionario;

import java.time.LocalDateTime;

public record CargoResponse(
    Long id,
    Long empresaId,
    String nome,
    String descricao,
    PapelFuncionario papel,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) { }
