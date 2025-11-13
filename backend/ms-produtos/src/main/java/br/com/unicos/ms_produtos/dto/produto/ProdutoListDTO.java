package br.com.unicos.ms_produtos.dto.produto;

import br.com.unicos.core.produto.model.ProdutoBase;

/**
 * DTO utilizado para listagem leve dos produtos.
 * Ideal para consultas rápidas e catálogos.
 */
public record ProdutoListDTO(

        Long id,
        ProdutoBase dadosBasicos,
        boolean ativo,
        Long categoriaId,
        Long marcaId

) {}
