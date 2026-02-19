package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.produtoimagem.ProdutoImagemCreateRequest;
import br.com.unicos.ms_compras.dto.produtoimagem.ProdutoImagemResponse;
import br.com.unicos.ms_compras.dto.produtoimagem.ProdutoImagemUpdateRequest;
import br.com.unicos.ms_compras.model.ProdutoImagem;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoImagemMapper {

    private final ModelMapper modelMapper;

    public ProdutoImagemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdutoImagem toEntity(ProdutoImagemCreateRequest request, Long empresaId) {
        ProdutoImagem entity = modelMapper.map(request, ProdutoImagem.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(ProdutoImagemUpdateRequest request, ProdutoImagem entity) {
        modelMapper.map(request, entity);
    }

    public ProdutoImagemResponse toResponse(ProdutoImagem entity) {
        return modelMapper.map(entity, ProdutoImagemResponse.class);
    }
}
