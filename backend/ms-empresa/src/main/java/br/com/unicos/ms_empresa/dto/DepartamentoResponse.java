package br.com.unicos.ms_empresa.dto;

import java.util.List;

public record DepartamentoResponse(
        Long id,
        String nome,
        Boolean ativo,
        EmpresaListDTO empresa,
        List<SetorResponse> setores
) {}