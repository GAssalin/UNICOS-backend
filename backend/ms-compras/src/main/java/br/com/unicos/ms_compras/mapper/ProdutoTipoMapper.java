package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.produtotipo.ProdutoTipoCreateRequest;
import br.com.unicos.ms_compras.dto.produtotipo.ProdutoTipoResponse;
import br.com.unicos.ms_compras.dto.produtotipo.ProdutoTipoResumoResponse;
import br.com.unicos.ms_compras.dto.produtotipo.ProdutoTipoUpdateRequest;
import br.com.unicos.ms_compras.model.ProdutoTipo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoTipoMapper {

    private final ModelMapper modelMapper;

    public ProdutoTipoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdutoTipo toEntity(ProdutoTipoCreateRequest request, Long empresaId) {
        ProdutoTipo entity = modelMapper.map(request, ProdutoTipo.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(ProdutoTipoUpdateRequest request, ProdutoTipo entity) {
        modelMapper.map(request, entity);
    }

    public ProdutoTipoResponse toResponse(ProdutoTipo entity) {
        return modelMapper.map(entity, ProdutoTipoResponse.class);
    }

    public ProdutoTipoResumoResponse toResumoResponse(ProdutoTipo entity) {
        return modelMapper.map(entity, ProdutoTipoResumoResponse.class);
    }
}
