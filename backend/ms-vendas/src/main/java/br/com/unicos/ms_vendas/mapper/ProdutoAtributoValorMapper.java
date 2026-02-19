package br.com.unicos.ms_vendas.mapper;

import br.com.unicos.ms_vendas.dto.produtoatributovalor.ProdutoAtributoValorCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoatributovalor.ProdutoAtributoValorResponse;
import br.com.unicos.ms_vendas.dto.produtoatributovalor.ProdutoAtributoValorUpdateRequest;
import br.com.unicos.ms_vendas.model.ProdutoAtributoValor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAtributoValorMapper {

    private final ModelMapper modelMapper;

    public ProdutoAtributoValorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdutoAtributoValor toEntity(ProdutoAtributoValorCreateRequest request, Long empresaId) {
        ProdutoAtributoValor entity = modelMapper.map(request, ProdutoAtributoValor.class);
        entity.setEmpresaId(empresaId);
        return entity;
    }

    public void updateEntity(ProdutoAtributoValorUpdateRequest request, ProdutoAtributoValor entity) {
        modelMapper.map(request, entity);
    }

    public ProdutoAtributoValorResponse toResponse(ProdutoAtributoValor entity) {
        return modelMapper.map(entity, ProdutoAtributoValorResponse.class);
    }
}
