package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoUpdateRequest;
import br.com.unicos.ms_produto.model.CategoriaProduto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoriaProdutoMapper {

    private final ModelMapper modelMapper;

    public CategoriaProdutoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoriaProduto toEntity(CategoriaProdutoCreateRequest request, Long empresaId) {
        CategoriaProduto entity = modelMapper.map(request, CategoriaProduto.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(CategoriaProdutoUpdateRequest request, CategoriaProduto entity) {
        modelMapper.map(request, entity);
    }

    public CategoriaProdutoResponse toResponse(CategoriaProduto entity) {
        return modelMapper.map(entity, CategoriaProdutoResponse.class);
    }

    public CategoriaProdutoResumoResponse toResumoResponse(CategoriaProduto entity) {
        return modelMapper.map(entity, CategoriaProdutoResumoResponse.class);
    }
}
