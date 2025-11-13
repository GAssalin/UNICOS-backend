package br.com.unicos.ms_produtos.dto.produto;

import br.com.unicos.core.produto.model.*;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorRequest;
import br.com.unicos.ms_produtos.dto.produto_variacao.ProdutoVariacaoRequest;

import java.util.List;

/**
 * DTO utilizado para criação ou atualização de um produto.
 *
 * Contém dados universais do produto (core-produto) e dados operacionais
 * pertencentes ao ms-produtos.
 */
public record ProdutoRequest(

        ProdutoBase dadosBasicos,
        ProdutoTributacaoBase tributacao,
        ProdutoEstoqueBase estoqueConfig,
        PrecoBase precoAtual,

        Long categoriaId,
        Long marcaId,

        List<ImagemProdutoRequest> imagens,
        List<ProdutoAtributoValorRequest> atributos,
        List<Long> fornecedoresIds,
        List<ProdutoVariacaoRequest> variacoes

) {}
