package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtoimagem.ProdutoImagemCreateRequest;
import br.com.unicos.ms_produto.dto.produtoimagem.ProdutoImagemResponse;
import br.com.unicos.ms_produto.dto.produtoimagem.ProdutoImagemUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoImagem;
import org.springframework.stereotype.Component;

@Component
public class ProdutoImagemMapper {

    public ProdutoImagem toEntity(ProdutoImagemCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        ProdutoImagem entity = new ProdutoImagem();
        entity.setProdutoId(request.produtoId());
        entity.setUrl(request.url());
        entity.setAltTexto(request.altTexto());
        entity.setPrincipal(request.principal());
        entity.setOrdem(request.ordem());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoImagemUpdateRequest request, ProdutoImagem entity) {
        if (request == null || entity == null)
            return;

        entity.setUrl(request.url());
        entity.setAltTexto(request.altTexto());
        entity.setPrincipal(request.principal());
        entity.setOrdem(request.ordem());
    }

    public ProdutoImagemResponse toResponse(ProdutoImagem entity) {
        if (entity == null)
            return null;

        return new ProdutoImagemResponse(
                entity.getId(),
                entity.getProdutoId(),
                entity.getUrl(),
                entity.getAltTexto(),
                entity.getPrincipal(),
                entity.getOrdem(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }
}