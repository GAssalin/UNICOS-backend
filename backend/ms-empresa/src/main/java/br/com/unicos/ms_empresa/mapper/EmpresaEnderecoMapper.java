package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre
 * EmpresaEndereco e seus DTOs utilizando ModelMapper.
 */
@Component
public class EmpresaEnderecoMapper {

    private final ModelMapper modelMapper;

    public EmpresaEnderecoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converte o DTO de criação para entidade.
     */
    public EmpresaEndereco toEntity(EmpresaEnderecoCreateRequest request) {
        EmpresaEndereco entity = modelMapper.map(request, EmpresaEndereco.class);

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
    public void updateEntity(EmpresaEnderecoUpdateRequest request, EmpresaEndereco entity) {
        modelMapper.map(request, entity);
    }

    /**
     * Converte entidade para DTO de resposta completa.
     */
    public EmpresaEnderecoResponse toResponse(EmpresaEndereco entity) {
        EmpresaEnderecoResponse response = modelMapper.map(entity, EmpresaEnderecoResponse.class);

        return new EmpresaEnderecoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getEmpresa().getId(),
                entity.getTipoEndereco(),
                response.logradouro(),
                response.numero(),
                response.complemento(),
                response.bairro(),
                response.municipio(),
                response.uf(),
                response.cep(),
                response.principal(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte entidade para DTO de listagem resumida.
     */
    public EmpresaEnderecoResumoResponse toResumoResponse(EmpresaEndereco entity) {
        return modelMapper.map(entity, EmpresaEnderecoResumoResponse.class);
    }
}
