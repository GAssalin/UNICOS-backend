package br.com.unicos.ms_empresa.dto.empresa_contato;

import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;

import java.time.LocalDateTime;

/**
 * DTO de resposta para contatos institucionais da empresa.
 */
public record EmpresaContatoResponse(
        Long id,
        TipoContatoEmpresa tipoContato,
        String valor,
        boolean principal,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) { }