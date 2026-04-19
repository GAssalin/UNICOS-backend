package br.com.unicos.ms_empresa.dto.empresa_configuracao;

import java.time.LocalDateTime;

/**
 * DTO de resposta para configurações da empresa.
 */
public record EmpresaConfiguracaoResponse(
        Long id,
        String chave,
        String valor,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) { }