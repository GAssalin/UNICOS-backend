package br.com.unicos.ms_vendas.mapper;

import br.com.unicos.ms_vendas.dto.marca.MarcaProdutoCreateRequest;
import br.com.unicos.ms_vendas.dto.marca.MarcaProdutoResponse;
import br.com.unicos.ms_vendas.dto.marca.MarcaProdutoResumoResponse;
import br.com.unicos.ms_vendas.dto.marca.MarcaProdutoUpdateRequest;
import br.com.unicos.ms_vendas.model.MarcaProduto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MarcaProdutoMapper {

    private final ModelMapper modelMapper;

    public MarcaProdutoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MarcaProduto toEntity(MarcaProdutoCreateRequest request, Long empresaId) {
        MarcaProduto entity = modelMapper.map(request, MarcaProduto.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(MarcaProdutoUpdateRequest request, MarcaProduto entity) {
        modelMapper.map(request, entity);
    }

    public MarcaProdutoResponse toResponse(MarcaProduto entity) {
        return modelMapper.map(entity, MarcaProdutoResponse.class);
    }

    public MarcaProdutoResumoResponse toResumoResponse(MarcaProduto entity) {
        return modelMapper.map(entity, MarcaProdutoResumoResponse.class);
    }
}
