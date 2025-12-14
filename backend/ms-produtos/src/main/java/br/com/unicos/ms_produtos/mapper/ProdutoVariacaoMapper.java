package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.produto_variacao.ProdutoVariacaoListDTO;
import br.com.unicos.ms_produtos.dto.produto_variacao.ProdutoVariacaoRequest;
import br.com.unicos.ms_produtos.dto.produto_variacao.ProdutoVariacaoResponse;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoVariacao;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre ProdutoVariacao
 * e seus respectivos DTOs.
 */
@Component
public class ProdutoVariacaoMapper {

    /**
     * Converte entidade para DTO de resposta detalhada.
     */
    public ProdutoVariacaoResponse toResponse(ProdutoVariacao entity) {

        Long produtoId = entity.getProduto() != null
                ? entity.getProduto().getId()
                : null;

        return new ProdutoVariacaoResponse(
                entity.getId(),
                produtoId,
                entity.getNome(),
                entity.getSku(),
                entity.getPreco(),
                entity.getCodigoBarras(),
                entity.getCor(),
                entity.getTamanho(),
                entity.getMaterial(),
                entity.getAtivo()
        );
    }

    /**
     * Converte entidade para DTO de listagem.
     */
    public ProdutoVariacaoListDTO toListDTO(ProdutoVariacao entity) {
        return new ProdutoVariacaoListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getSku(),
                entity.getPreco(),
                entity.getAtivo()
        );
    }

    /**
     * Constrói entidade ProdutoVariacao a partir do request.
     *
     * <p>
     * O produto deve ser fornecido pelo service já carregado.
     * </p>
     */
    public ProdutoVariacao toEntity(
            ProdutoVariacaoRequest request,
            Produto produto
    ) {
        return ProdutoVariacao.builder()
                .produto(produto)
                .nome(request.nome())
                .sku(request.sku())
                .preco(request.preco())
                .codigoBarras(request.codigoBarras())
                .cor(request.cor())
                .tamanho(request.tamanho())
                .material(request.material())
                .ativo(true)
                .build();
    }

    /**
     * Atualiza entidade existente com dados do request.
     */
    public void updateEntity(
            ProdutoVariacaoRequest request,
            ProdutoVariacao entity
    ) {
        entity.setNome(request.nome());
        entity.setSku(request.sku());
        entity.setPreco(request.preco());
        entity.setCodigoBarras(request.codigoBarras());
        entity.setCor(request.cor());
        entity.setTamanho(request.tamanho());
        entity.setMaterial(request.material());
    }
}
