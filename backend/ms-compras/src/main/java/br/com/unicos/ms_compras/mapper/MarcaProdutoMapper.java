package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.marca.MarcaProdutoCreateRequest;
import br.com.unicos.ms_compras.dto.marca.MarcaProdutoResponse;
import br.com.unicos.ms_compras.dto.marca.MarcaProdutoResumoResponse;
import br.com.unicos.ms_compras.dto.marca.MarcaProdutoUpdateRequest;
import br.com.unicos.ms_compras.model.MarcaProduto;
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
