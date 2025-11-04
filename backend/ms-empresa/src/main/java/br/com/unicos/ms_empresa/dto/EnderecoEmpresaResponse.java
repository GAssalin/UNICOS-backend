package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;

public record EnderecoEmpresaResponse(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        TipoEnderecoEmpresa tipoEndereco
) {}