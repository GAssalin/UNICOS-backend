package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseCreateRequest;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseResponse;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoPrecoBase;
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
