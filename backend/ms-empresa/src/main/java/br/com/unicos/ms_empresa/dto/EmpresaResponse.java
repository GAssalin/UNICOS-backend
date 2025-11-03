package br.com.unicos.ms_empresa.dto;

import java.util.List;

/**
 * DTO usado para retorno detalhado das informações de uma empresa.
 */
public record EmpresaResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String inscricaoEstadual,
        String inscricaoMunicipal,
        List<FilialListDTO> filiais,
        List<EnderecoEmpresaListDTO> enderecos,
        List<ContatoEmpresaListDTO> contatos,
        List<DepartamentoEmpresaListDTO> departamentos
) {}