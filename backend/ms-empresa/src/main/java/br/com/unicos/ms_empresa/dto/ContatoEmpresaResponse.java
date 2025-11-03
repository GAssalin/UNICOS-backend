package br.com.unicos.ms_empresa.dto;

/**
 * DTO usado para retorno detalhado das informações de um contato empresarial.
 */
public record ContatoEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaRazaoSocial,
        String nomeContato,
        String cargo,
        String telefone,
        String celular,
        String email
) {}