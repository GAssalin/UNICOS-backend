package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;

/**
 * DTO usado para retorno detalhado das informações de um endereço empresarial.
 */
public record EnderecoEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaRazaoSocial,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep,
        TipoEnderecoEmpresa tipo
) {}