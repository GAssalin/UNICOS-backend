package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaParametro;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre
 * EmpresaParametro e seus DTOs utilizando ModelMapper.
 */
@Component
public class EmpresaParametroMapper {

    private final ModelMapper modelMapper;

    public EmpresaParametroMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converte o DTO de criação para entidade.
     */
    public EmpresaParametro toEntity(EmpresaParametroCreateRequest request) {
        EmpresaParametro entity = modelMapper.map(request, EmpresaParametro.class);

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
    public void updateEntity(EmpresaParametroUpdateRequest request, EmpresaParametro entity) {
        modelMapper.map(request, entity);
    }

    /**
     * Converte entidade para DTO de resposta completa.
     */
    public EmpresaParametroResponse toResponse(EmpresaParametro entity) {
        EmpresaParametroResponse response = modelMapper.map(entity, EmpresaParametroResponse.class);

        return new EmpresaParametroResponse(
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
    public EmpresaParametroResumoResponse toResumoResponse(EmpresaParametro entity) {
        return modelMapper.map(entity, EmpresaParametroResumoResponse.class);
    }
}
