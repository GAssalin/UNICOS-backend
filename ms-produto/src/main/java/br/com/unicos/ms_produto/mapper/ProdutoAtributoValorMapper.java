package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorCreateRequest;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorResponse;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoAtributoValor;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAtributoValorMapper {

    public ProdutoAtributoValor toEntity(ProdutoAtributoValorCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        ProdutoAtributoValor entity = new ProdutoAtributoValor();
        entity.setProdutoId(request.produtoId());
        entity.setAtributoId(request.atributoId());
        entity.setValor(request.valor());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoAtributoValorUpdateRequest request, ProdutoAtributoValor entity) {
        if (request == null || entity == null)
            return;

        entity.setValor(request.valor());
    }

    public ProdutoAtributoValorResponse toResponse(ProdutoAtributoValor entity) {
        if (entity == null)
            return null;

        return new ProdutoAtributoValorResponse(
                entity.getId(),
                entity.getProdutoId(),
                entity.getAtributoId(),
                entity.getValor(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }
}