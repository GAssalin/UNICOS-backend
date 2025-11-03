package br.com.unicos.ms_empresa.dto;

/**
 * DTO usado para retorno detalhado das informações de um departamento empresarial.
 */
public record DepartamentoEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaRazaoSocial,
        String nome,
        String descricao,
        Boolean ativo
) {}