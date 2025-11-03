package br.com.unicos.ms_empresa.dto;

/**
 * DTO usado para retorno detalhado das informações de uma filial.
 */
public record FilialResponse(
        Long id,
        Long empresaId,
        String empresaMatrizNome,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String inscricaoEstadual,
        String inscricaoMunicipal,
        Boolean matriz,
        String telefone,
        String email,
        String endereco,
        String cidade,
        String uf,
        String cep
) {}