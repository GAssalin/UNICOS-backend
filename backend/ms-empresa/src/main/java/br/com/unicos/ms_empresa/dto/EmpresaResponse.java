package br.com.unicos.ms_empresa.dto;

import java.util.List;

public record EmpresaResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String inscricaoEstadual,
        String inscricaoMunicipal,
        List<FilialResponse> filiais,
        List<EnderecoEmpresaResponse> enderecos,
        List<DepartamentoResponse> departamentos,
        List<ContatoEmpresaResponse> contatos,
        ConfiguracaoFiscalResponse configuracaoFiscal
) {}