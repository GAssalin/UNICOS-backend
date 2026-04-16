package br.com.unicos.ms_empresa.dto.empresa_endereco;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;

import java.time.LocalDateTime;

/**
 * DTO de resposta para endereços institucionais da empresa.
 */
public record EmpresaEnderecoResponse(
        Long id,
        Long empresaId,
        Long empresaRefId,
        TipoEnderecoEmpresa tipoEndereco,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String municipio,
        String uf,
        String cep,
        boolean principal,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) { }
