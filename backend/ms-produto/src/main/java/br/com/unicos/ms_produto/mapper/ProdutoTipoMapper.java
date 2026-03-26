package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoCreateRequest;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResumoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoUpdateRequest;
import br.com.unicos.ms_produto.model.ProdutoTipo;
import org.springframework.stereotype.Component;

@Component
public class ProdutoTipoMapper {

    public ProdutoTipo toEntity(ProdutoTipoCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        ProdutoTipo entity = new ProdutoTipo();
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(ProdutoTipoUpdateRequest request, ProdutoTipo entity) {
        if (request == null || entity == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }

    public ProdutoTipoResponse toResponse(ProdutoTipo entity) {
        if (entity == null)
            return null;

        return new ProdutoTipoResponse(
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

    public ProdutoTipoResumoResponse toResumoResponse(ProdutoTipo entity) {
        if (entity == null)
            return null;

        return new ProdutoTipoResumoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo()
        );
    }
}