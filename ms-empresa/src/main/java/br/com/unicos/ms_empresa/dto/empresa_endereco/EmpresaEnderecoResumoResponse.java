package br.com.unicos.ms_empresa.dto.empresa_endereco;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;

/**
 * DTO simplificado para listagem de endereços institucionais.
 */
public record EmpresaEnderecoResumoResponse(
        Long id,
        TipoEnderecoEmpresa tipoEndereco,
        String municipio,
        String uf,
        boolean principal
) { }
