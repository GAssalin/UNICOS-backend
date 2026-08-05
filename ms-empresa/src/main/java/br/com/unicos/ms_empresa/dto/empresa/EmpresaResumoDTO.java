package br.com.unicos.ms_empresa.dto.empresa;

import br.com.unicos.ms_empresa.enums.StatusEmpresa;

/**
 * DTO resumido da empresa.
 * Utilizado para listagens simples e referências externas.
 */
public record EmpresaResumoDTO(
        Long id,
        Long empresaId,
        String razaoSocial,
        String cnpj,
        StatusEmpresa statusEmpresa
) { }
