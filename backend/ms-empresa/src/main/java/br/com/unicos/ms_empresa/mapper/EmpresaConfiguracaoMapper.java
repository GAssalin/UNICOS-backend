package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaConfiguracao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre
 * EmpresaConfiguracao e seus DTOs utilizando ModelMapper.
 */
@Component
public class EmpresaConfiguracaoMapper {

    private final ModelMapper modelMapper;

    public EmpresaConfiguracaoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converte o DTO de criação para entidade.
     */
    public EmpresaConfiguracao toEntity(EmpresaConfiguracaoCreateRequest request) {
        EmpresaConfiguracao entity = modelMapper.map(request, EmpresaConfiguracao.class);

        // Tenant
        entity.setEmpresaId(request.empresaId());

        // Empresa de referência (apenas ID)
        entity.setEmpresa(
                Empresa.builder()
                        .id(request.empresaRefId())
                        .build()
        );

        return entity;
    }

    /**
     * Atualiza uma entidade existente a partir do DTO de atualização.
     */
    public void updateEntity(EmpresaConfiguracaoUpdateRequest request, EmpresaConfiguracao entity) {
        modelMapper.map(request, entity);
    }

    /**
     * Converte entidade para DTO de resposta completa.
     */
    public EmpresaConfiguracaoResponse toResponse(EmpresaConfiguracao entity) {
        EmpresaConfiguracaoResponse response = modelMapper.map(entity, EmpresaConfiguracaoResponse.class);

        return new EmpresaConfiguracaoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getEmpresa().getId(),
                response.chave(),
                response.valor(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte entidade para DTO de listagem resumida.
     */
    public EmpresaConfiguracaoResumoResponse toResumoResponse(EmpresaConfiguracao entity) {
        return modelMapper.map(entity, EmpresaConfiguracaoResumoResponse.class);
    }
}
