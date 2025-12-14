package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoListDTO;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoRequest;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoResponse;
import br.com.unicos.ms_produtos.model.ImagemProduto;
import br.com.unicos.ms_produtos.model.Produto;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre ImagemProduto
 * e seus respectivos DTOs.
 *
 * <p>
 * Não realiza acesso a banco de dados. O carregamento de entidades
 * é responsabilidade da camada de service.
 * </p>
 */
@Component
public class ImagemProdutoMapper {

    /**
     * Converte entidade para DTO de resposta detalhada.
     */
    public ImagemProdutoResponse toResponse(ImagemProduto entity) {

        Long produtoId = entity.getProduto() != null
                ? entity.getProduto().getId()
                : null;

        return new ImagemProdutoResponse(
                entity.getId(),
                produtoId,
                entity.getUrl(),
                entity.getDescricaoAlt(),
                entity.getPrincipal(),
                entity.getOrdemExibicao(),
                entity.getAtivo()
        );
    }

    /**
     * Converte entidade para DTO de listagem.
     */
    public ImagemProdutoListDTO toListDTO(ImagemProduto entity) {
        return new ImagemProdutoListDTO(
                entity.getId(),
                entity.getUrl(),
                entity.getPrincipal(),
                entity.getOrdemExibicao()
        );
    }

    /**
     * Constrói entidade ImagemProduto a partir do request.
     */
    public ImagemProduto toEntity(
            ImagemProdutoRequest request,
            Produto produto
    ) {
        return ImagemProduto.builder()
                .produto(produto)
                .url(request.url())
                .descricaoAlt(request.descricaoAlt())
                .principal(request.principal())
                .ordemExibicao(request.ordemExibicao())
                .ativo(true)
                .build();
    }

    /**
     * Atualiza entidade existente com dados do request.
     */
    public void updateEntity(
            ImagemProdutoRequest request,
            ImagemProduto entity
    ) {
        entity.setUrl(request.url());
        entity.setDescricaoAlt(request.descricaoAlt());
        entity.setPrincipal(request.principal());
        entity.setOrdemExibicao(request.ordemExibicao());
    }
}
