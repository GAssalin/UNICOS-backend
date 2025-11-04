package br.com.unicos.ms_empresa.dto;

public record EnderecoEmpresaListDTO(
        Long id,
        String cidade,
        String estado,
        TipoEnderecoEmpresa tipoEndereco
) {}