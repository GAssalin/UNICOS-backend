package br.com.unicos.ms_vendas.mapper;

import br.com.unicos.ms_vendas.dto.produtotipo.ProdutoTipoCreateRequest;
import br.com.unicos.ms_vendas.dto.produtotipo.ProdutoTipoResponse;
import br.com.unicos.ms_vendas.dto.produtotipo.ProdutoTipoResumoResponse;
import br.com.unicos.ms_vendas.dto.produtotipo.ProdutoTipoUpdateRequest;
import br.com.unicos.ms_vendas.model.ProdutoTipo;
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
