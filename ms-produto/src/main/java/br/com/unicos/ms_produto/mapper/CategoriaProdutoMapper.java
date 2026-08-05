package br.com.unicos.ms_produto.mapper;

import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoUpdateRequest;
import br.com.unicos.ms_produto.model.CategoriaProduto;
import org.springframework.stereotype.Component;

@Component
public class CategoriaProdutoMapper {

    /**
     * Converte o request de criação em entidade.
     *
     * @param request   dados recebidos na requisição
     * @param empresaId identificador da empresa logada
     * @return entidade preenchida manualmente
     */
    public CategoriaProduto toEntity(CategoriaProdutoCreateRequest request, Long empresaId) {
        if (request == null)
            return null;

        CategoriaProduto entity = new CategoriaProduto();
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setCategoriaPaiId(request.categoriaPaiId());
        entity.setEmpresaId(empresaId);

        return entity;
    }

    /**
     * Atualiza manualmente os dados da entidade com base no request.
     *
     * @param request dados recebidos para atualização
     * @param entity  entidade que será atualizada
     */
    public void updateEntity(CategoriaProdutoUpdateRequest request, CategoriaProduto entity) {
        if (request == null || entity == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setCategoriaPaiId(request.categoriaPaiId());
    }

    /**
     * Converte a entidade em response detalhado.
     *
     * @param entity entidade de categoria de produto
     * @return response detalhado
     */
    public CategoriaProdutoResponse toResponse(CategoriaProduto entity) {
        if (entity == null)
            return null;

        return new CategoriaProdutoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getCategoriaPaiId(),
                entity.getAtivo(),
                entity.getCriadoPor(),
                entity.getCriadoEm(),
                entity.getAtualizadoPor(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte a entidade em response resumido.
     *
     * @param entity entidade de categoria de produto
     * @return response resumido
     */
    public CategoriaProdutoResumoResponse toResumoResponse(CategoriaProduto entity) {
        if (entity == null)
            return null;

        return new CategoriaProdutoResumoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getAtivo()
        );
    }
}