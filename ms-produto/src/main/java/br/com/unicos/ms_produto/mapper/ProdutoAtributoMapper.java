package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoCreateRequest;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoResponse;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoResumoResponse;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoAtributo;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAtributoMapper {

    public ProdutoAtributo toEntity(ProdutoAtributoCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        ProdutoAtributo entity = new ProdutoAtributo();
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoAtributoUpdateRequest request, ProdutoAtributo entity) {
        if (request == null || entity == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }

    public ProdutoAtributoResponse toResponse(ProdutoAtributo entity) {
        if (entity == null)
            return null;

        return new ProdutoAtributoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }

    public ProdutoAtributoResumoResponse toResumoResponse(ProdutoAtributo entity) {
        if (entity == null)
            return null;

        return new ProdutoAtributoResumoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo()
        );
    }
}