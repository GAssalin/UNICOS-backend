package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaUsuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre
 * EmpresaUsuario e seus DTOs utilizando ModelMapper.
 */
@Component
public class EmpresaUsuarioMapper {

    private final ModelMapper modelMapper;

    public EmpresaUsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converte o DTO de criação para entidade.
     */
    public EmpresaUsuario toEntity(EmpresaUsuarioCreateRequest request) {
        EmpresaUsuario entity = modelMapper.map(request, EmpresaUsuario.class);

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
     * <p>
     * Apenas o perfil é alterável.
     * </p>
     */
    public void updateEntity(EmpresaUsuarioUpdateRequest request, EmpresaUsuario entity) {
        modelMapper.map(request, entity);
    }

    /**
     * Converte entidade para DTO de resposta completa.
     */
    public EmpresaUsuarioResponse toResponse(EmpresaUsuario entity) {
        EmpresaUsuarioResponse response = modelMapper.map(entity, EmpresaUsuarioResponse.class);

        return new EmpresaUsuarioResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getEmpresa().getId(),
                entity.getUsuarioId(),
                response.perfil(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte entidade para DTO de listagem resumida.
     */
    public EmpresaUsuarioResumoResponse toResumoResponse(EmpresaUsuario entity) {
        return modelMapper.map(entity, EmpresaUsuarioResumoResponse.class);
    }
}
