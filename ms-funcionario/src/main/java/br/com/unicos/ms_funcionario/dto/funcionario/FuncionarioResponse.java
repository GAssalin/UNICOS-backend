package br.com.unicos.ms_funcionario.dto.funcionario;

import br.com.unicos.core.funcionario.enums.EscopoCarteira;
import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import br.com.unicos.ms_funcionario.enums.StatusFuncionario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FuncionarioResponse(
    Long id,
    Long empresaId,
    Long pessoaId,
    Long usuarioId,
    String matricula,
    Long cargoId,
    String cargoNome,
    PapelFuncionario papel,
    EscopoCarteira escopoCarteira,
    Long superiorId,
    Long filialId,
    LocalDate dataAdmissao,
    LocalDate dataDesligamento,
    StatusFuncionario status,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) { }
