package br.com.unicos.ms_empresa.dto.empresa;

import br.com.unicos.ms_empresa.enums.RegimeTributario;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;

import java.time.LocalDate;

/**
 * DTO de resposta com dados públicos da empresa.
 */
public record EmpresaResponseDTO(
        Long id,
        Long empresaId,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        TipoEmpresa tipoEmpresa,
        StatusEmpresa statusEmpresa,
        RegimeTributario regimeTributario,
        LocalDate dataAbertura,
        Long pessoaJuridicaId
) { }
