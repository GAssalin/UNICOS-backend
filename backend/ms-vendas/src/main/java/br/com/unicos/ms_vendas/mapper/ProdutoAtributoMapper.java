package br.com.unicos.ms_vendas.mapper;

import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoResponse;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoResumoResponse;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoUpdateRequest;
import br.com.unicos.ms_vendas.model.ProdutoAtributo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAtributoMapper {

    private final ModelMapper modelMapper;

    public ProdutoAtributoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdutoAtributo toEntity(ProdutoAtributoCreateRequest request, Long empresaId) {
        ProdutoAtributo entity = modelMapper.map(request, ProdutoAtributo.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(ProdutoAtributoUpdateRequest request, ProdutoAtributo entity) {
        modelMapper.map(request, entity);
    }

    public ProdutoAtributoResponse toResponse(ProdutoAtributo entity) {
        return modelMapper.map(entity, ProdutoAtributoResponse.class);
    }

    public ProdutoAtributoResumoResponse toResumoResponse(ProdutoAtributo entity) {
        return modelMapper.map(entity, ProdutoAtributoResumoResponse.class);
    }
}
