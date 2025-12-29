package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre
 * EmpresaContato e seus DTOs utilizando ModelMapper.
 */
@Component
public class EmpresaContatoMapper {

    private final ModelMapper modelMapper;

    public EmpresaContatoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converte o DTO de criação para entidade.
     */
    public EmpresaContato toEntity(EmpresaContatoCreateRequest request) {
        EmpresaContato entity = modelMapper.map(request, EmpresaContato.class);

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
    public void updateEntity(EmpresaContatoUpdateRequest request, EmpresaContato entity) {
        modelMapper.map(request, entity);
    }

    /**
     * Converte entidade para DTO de resposta completa.
     */
    public EmpresaContatoResponse toResponse(EmpresaContato entity) {
        EmpresaContatoResponse response = modelMapper.map(entity, EmpresaContatoResponse.class);

        return new EmpresaContatoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getEmpresa().getId(),
                entity.getTipoContato(),
                response.valor(),
                response.principal(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte entidade para DTO de listagem resumida.
     */
    public EmpresaContatoResumoResponse toResumoResponse(EmpresaContato entity) {
        return modelMapper.map(entity, EmpresaContatoResumoResponse.class);
    }
}
