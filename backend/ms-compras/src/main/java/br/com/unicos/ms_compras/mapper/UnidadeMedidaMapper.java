package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaCreateRequest;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaResponse;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaResumoResponse;
import br.com.unicos.ms_compras.dto.unidademedida.UnidadeMedidaUpdateRequest;
import br.com.unicos.ms_compras.model.UnidadeMedida;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UnidadeMedidaMapper {

    private final ModelMapper modelMapper;

    public UnidadeMedidaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UnidadeMedida toEntity(UnidadeMedidaCreateRequest request, Long empresaId) {
        UnidadeMedida entity = modelMapper.map(request, UnidadeMedida.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(UnidadeMedidaUpdateRequest request, UnidadeMedida entity) {
        modelMapper.map(request, entity);
    }

    public UnidadeMedidaResponse toResponse(UnidadeMedida entity) {
        return modelMapper.map(entity, UnidadeMedidaResponse.class);
    }

    public UnidadeMedidaResumoResponse toResumoResponse(UnidadeMedida entity) {
        return modelMapper.map(entity, UnidadeMedidaResumoResponse.class);
    }
}
