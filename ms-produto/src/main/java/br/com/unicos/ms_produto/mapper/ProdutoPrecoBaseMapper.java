package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseCreateRequest;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseResponse;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoPrecoBase;
import org.springframework.stereotype.Component;

@Component
public class ProdutoPrecoBaseMapper {

    public ProdutoPrecoBase toEntity(ProdutoPrecoBaseCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        ProdutoPrecoBase entity = new ProdutoPrecoBase();
        entity.setProdutoId(request.produtoId());
        entity.setCustoBase(request.custoBase());
        entity.setPrecoVendaBase(request.precoVendaBase());
        entity.setMargemBase(request.margemBase());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoPrecoBaseUpdateRequest request, ProdutoPrecoBase entity) {
        if (request == null || entity == null)
            return;

        entity.setCustoBase(request.custoBase());
        entity.setPrecoVendaBase(request.precoVendaBase());
        entity.setMargemBase(request.margemBase());
    }

    public ProdutoPrecoBaseResponse toResponse(ProdutoPrecoBase entity) {
        if (entity == null)
            return null;

        return new ProdutoPrecoBaseResponse(
                entity.getId(),
                entity.getProdutoId(),
                entity.getCustoBase(),
                entity.getPrecoVendaBase(),
                entity.getMargemBase(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }
}