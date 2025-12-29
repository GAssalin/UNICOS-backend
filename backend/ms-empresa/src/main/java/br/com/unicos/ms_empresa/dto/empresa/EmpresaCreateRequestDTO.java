package br.com.unicos.ms_empresa.dto.empresa;

import br.com.unicos.ms_empresa.enums.RegimeTributario;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação de uma empresa no UniCoS.
 */
public record EmpresaCreateRequestDTO(
        @NotBlank
        String razaoSocial,
        String nomeFantasia,
        @NotBlank
        String cnpj,
        @NotNull
        TipoEmpresa tipoEmpresa,
        @NotNull
        StatusEmpresa statusEmpresa,
        @NotNull
        RegimeTributario regimeTributario,
        LocalDate dataAbertura,
        Long pessoaJuridicaId
) { }
