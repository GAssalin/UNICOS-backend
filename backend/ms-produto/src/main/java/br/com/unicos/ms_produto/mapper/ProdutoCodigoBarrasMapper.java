package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasCreateRequest;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasResponse;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoCodigoBarras;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoCodigoBarrasMapper {

    private final ModelMapper modelMapper;

    public ProdutoCodigoBarrasMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdutoCodigoBarras toEntity(ProdutoCodigoBarrasCreateRequest request, Long empresaId) {
        ProdutoCodigoBarras entity = modelMapper.map(request, ProdutoCodigoBarras.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(ProdutoCodigoBarrasUpdateRequest request, ProdutoCodigoBarras entity) {
        modelMapper.map(request, entity);
    }

    public ProdutoCodigoBarrasResponse toResponse(ProdutoCodigoBarras entity) {
        return modelMapper.map(entity, ProdutoCodigoBarrasResponse.class);
    }
}
