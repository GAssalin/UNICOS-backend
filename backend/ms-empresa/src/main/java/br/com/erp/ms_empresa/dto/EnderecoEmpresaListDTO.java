package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;

/**
 * DTO usado para listagem simplificada dos endereços de uma empresa.
 */
public record EnderecoEmpresaListDTO(
        Long id,
        String logradouro,
        String numero,
        String cidade,
        String uf,
        TipoEnderecoEmpresa tipo
) {}