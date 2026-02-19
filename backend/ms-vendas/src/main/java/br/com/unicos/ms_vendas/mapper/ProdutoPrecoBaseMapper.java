package br.com.unicos.ms_vendas.mapper;

import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseResponse;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseUpdateRequest;
import br.com.unicos.ms_vendas.model.ProdutoPrecoBase;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoPrecoBaseMapper {

    private final ModelMapper modelMapper;

    public ProdutoPrecoBaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdutoPrecoBase toEntity(ProdutoPrecoBaseCreateRequest request, Long empresaId) {
        ProdutoPrecoBase entity = modelMapper.map(request, ProdutoPrecoBase.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(ProdutoPrecoBaseUpdateRequest request, ProdutoPrecoBase entity) {
        modelMapper.map(request, entity);
    }

    public ProdutoPrecoBaseResponse toResponse(ProdutoPrecoBase entity) {
        return modelMapper.map(entity, ProdutoPrecoBaseResponse.class);
    }
}
