package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasCreateRequest;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasResponse;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoCodigoBarras;
import org.springframework.stereotype.Component;

@Component
public class ProdutoCodigoBarrasMapper {

    public ProdutoCodigoBarras toEntity(ProdutoCodigoBarrasCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        ProdutoCodigoBarras entity = new ProdutoCodigoBarras();
        entity.setProdutoId(request.produtoId());
        entity.setCodigoBarras(request.codigoBarras());
        entity.setPrincipal(request.principal());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoCodigoBarrasUpdateRequest request, ProdutoCodigoBarras entity) {
        if (request == null || entity == null)
            return;

        entity.setCodigoBarras(request.codigoBarras());
        entity.setPrincipal(request.principal());
    }

    public ProdutoCodigoBarrasResponse toResponse(ProdutoCodigoBarras entity) {
        if (entity == null)
            return null;

        return new ProdutoCodigoBarrasResponse(
                entity.getId(),
                entity.getProdutoId(),
                entity.getCodigoBarras(),
                entity.getPrincipal(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }
}