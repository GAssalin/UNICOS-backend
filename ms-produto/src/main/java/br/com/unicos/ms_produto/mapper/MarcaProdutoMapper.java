package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.marca.MarcaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoResponse;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoListDTO;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoUpdateRequest;
import br.com.unicos.ms_produto.model.MarcaProduto;
import org.springframework.stereotype.Component;

@Component
public class MarcaProdutoMapper {

    public MarcaProduto toEntity(MarcaProdutoCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        MarcaProduto entity = new MarcaProduto();
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    public void updateEntity(MarcaProduto entity, MarcaProdutoUpdateRequest request) {
        if (request == null || entity == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }

    public MarcaProdutoResponse toResponse(MarcaProduto entity) {
        if (entity == null)
            return null;

        return new MarcaProdutoResponse(
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

    public MarcaProdutoListDTO toListDTO(MarcaProduto entity) {
        if (entity == null)
            return null;

        return new MarcaProdutoListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo()
        );
    }
}