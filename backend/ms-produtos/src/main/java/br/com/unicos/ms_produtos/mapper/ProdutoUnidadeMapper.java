package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeListDTO;
import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeRequest;
import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeResponse;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoUnidade;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre ProdutoUnidade
 * e seus respectivos DTOs.
 */
@Component
public class ProdutoUnidadeMapper {

    /**
     * Converte entidade para DTO de resposta detalhada.
     */
    public ProdutoUnidadeResponse toResponse(ProdutoUnidade entity) {

        Long produtoId = entity.getProduto() != null
                ? entity.getProduto().getId()
                : null;

        String produtoNome = entity.getProduto() != null
                ? entity.getProduto().getDadosBasicos().getNome()
                : null;

        Long unidadeId = entity.getUnidadeMedida() != null
                ? entity.getUnidadeMedida().getId()
                : null;

        String unidadeNome = entity.getUnidadeMedida() != null
                ? entity.getUnidadeMedida().getNome()
                : null;

        return new ProdutoUnidadeResponse(
                entity.getId(),
                produtoId,
                produtoNome,
                unidadeId,
                unidadeNome,
                entity.getQuantidadePadrao(),
                entity.getFatorConversao()
        );
    }

    /**
     * Converte entidade para DTO de listagem.
     */
    public ProdutoUnidadeListDTO toListDTO(ProdutoUnidade entity) {

        String produtoNome = entity.getProduto() != null
                ? entity.getProduto().getDadosBasicos().getNome()
                : null;

        String unidadeNome = entity.getUnidadeMedida() != null
                ? entity.getUnidadeMedida().getNome()
                : null;

        return new ProdutoUnidadeListDTO(
                entity.getId(),
                produtoNome,
                unidadeNome,
                entity.getQuantidadePadrao()
        );
    }

    /**
     * Constrói entidade ProdutoUnidade a partir do request.
     */
    public ProdutoUnidade toEntity(
            ProdutoUnidadeRequest request,
            Produto produto,
            UnidadeMedida unidadeMedida,
            Long empresaId
    ) {
        return ProdutoUnidade.builder()
                .empresaId(empresaId)
                .produto(produto)
                .unidadeMedida(unidadeMedida)
                .quantidadePadrao(request.quantidadePadrao())
                .fatorConversao(
                        request.fatorConversao() != null
                                ? request.fatorConversao()
                                : 1.0
                )
                .build();
    }
}
