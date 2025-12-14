package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorListDTO;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorRequest;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorResponse;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoAtributoValor;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre ProdutoAtributoValor
 * e seus respectivos DTOs.
 */
@Component
public class ProdutoAtributoValorMapper {

    /**
     * Converte entidade para DTO de resposta detalhada.
     */
    public ProdutoAtributoValorResponse toResponse(
            ProdutoAtributoValor entity,
            Long produtoId
    ) {

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
     * Converte entidade para DTO de listagem.
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
     * Converte request para entidade ProdutoAtributoValor.
     */
    public ProdutoAtributoValor toEntity(
            ProdutoAtributoValorRequest request,
            Produto produto,
            AtributoPersonalizado atributoPersonalizado
    ) {
        return ProdutoAtributoValor.builder()
                .produto(produto)
                .atributoPersonalizado(atributoPersonalizado)
                .valor(request.valor())
                .build();
    }
}
