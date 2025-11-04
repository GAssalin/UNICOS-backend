package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;

public record EnderecoEmpresaListDTO(
        Long id,
        String cidade,
        String estado,
        TipoEnderecoEmpresa tipoEndereco
) {}