package br.com.unicos.ms_empresa.dto.empresa_parametro;

import java.time.LocalDateTime;

/**
 * DTO de resposta para parâmetros da empresa.
 */
public record EmpresaParametroResponse(
        Long id,
        Long empresaId,
        Long empresaRefId,
        String chave,
        String valor,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) { }
