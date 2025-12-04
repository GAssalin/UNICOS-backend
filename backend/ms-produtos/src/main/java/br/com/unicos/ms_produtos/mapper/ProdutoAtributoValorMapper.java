package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorListDTO;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorRequest;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorResponse;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoAtributoValor;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por converter entidades ProdutoAtributoValor
 * para seus respectivos DTOs (List, Response) e também construir a
 * entidade a partir de um request.
 */
@Component
public class ProdutoAtributoValorMapper {

    /**
     * Converte entidade → DTO completo (ProdutoAtributoValorResponse).
     *
     * @param entity    entidade ProdutoAtributoValor
     * @param produtoId ID do produto pai
     */
    public ProdutoAtributoValorResponse toResponse(ProdutoAtributoValor entity, Long produtoId) {

        Long atributoId = entity.getAtributoPersonalizado() != null
                ? entity.getAtributoPersonalizado().getId()
                : null;

        String atributoNome = entity.getAtributoPersonalizado() != null
                ? entity.getAtributoPersonalizado().getNome()
                : null;

        return new ProdutoAtributoValorResponse(
                entity.getId(),
                produtoId,
                atributoId,
                atributoNome,
                entity.getValor()
        );
    }

    /**
     * Converte entidade → DTO simplificado (ProdutoAtributoValorListDTO).
     */
    public ProdutoAtributoValorListDTO toListDTO(ProdutoAtributoValor entity) {

        String atributoNome = entity.getAtributoPersonalizado() != null
                ? entity.getAtributoPersonalizado().getNome()
                : null;

        return new ProdutoAtributoValorListDTO(
                entity.getId(),
                atributoNome,
                entity.getValor()
        );
    }

    /**
     * Converte request → entidade ProdutoAtributoValor.
     * <p>
     * Não carrega entidades do banco aqui — o service deve fornecer:
     * - Produto já carregado
     * - AtributoPersonalizado já carregado
     */
    public ProdutoAtributoValor toEntity(
            ProdutoAtributoValorRequest request,
            Produto produto,
            AtributoPersonalizado atributoPersonalizado) {

        return ProdutoAtributoValor.builder()
                .produto(produto)
                .atributoPersonalizado(atributoPersonalizado)
                .valor(request.valor())
                .build();
    }
}
