package br.com.unicos.ms_empresa.dto.empresa_contato;

import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;

/**
 * DTO simplificado para listagem de contatos institucionais.
 */
public record EmpresaContatoListDTO(
        Long id,
        TipoContatoEmpresa tipoContato,
        String valor,
        boolean principal
) { }
