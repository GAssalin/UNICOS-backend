package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoListDTO;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoRequest;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoResponse;
import br.com.unicos.ms_produtos.model.ImagemProduto;
import br.com.unicos.ms_produtos.model.Produto;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por converter entidades ImagemProduto
 * para seus respectivos DTOs (List, Response) e também
 * construir entidades a partir de requests.
 *
 * <p>
 * Não realiza acesso ao banco — isso é responsabilidade do service.
 * </p>
 */
@Component
public class ImagemProdutoMapper {

    /**
     * Converte entidade → DTO completo (ImagemProdutoResponse).
     *
     * @param entity entidade ImagemProduto
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
     * Converte entidade → DTO simplificado (ImagemProdutoListDTO).
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
     * Constrói a entidade ImagemProduto a partir do request.
     * <p>
     * O service deve fornecer o Produto já carregado.
     * </p>
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
                .ativo(true) // padrão
                .build();
    }

    /**
     * Atualiza uma entidade existente com dados do request.
     * <p>
     * Mantém o vínculo com o Produto.
     * </p>
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
