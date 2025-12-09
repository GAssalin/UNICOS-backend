package br.com.unicos.ms_produtos.dto.produto;

import br.com.unicos.core.produto.model.*;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoResponse;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorResponse;
import br.com.unicos.ms_produtos.dto.produto_variacao.ProdutoVariacaoResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta que representa todas as informações de um produto.
 * Inclui dados universais e dados operacionais.
 */
public record ProdutoResponse(
        Long id,
        boolean ativo,

        ProdutoBase dadosBasicos,
        ProdutoTributacaoBase tributacao,
        PrecoBase precoAtual,

        CategoriaResponse categoria,
        MarcaResponse marca,

        List<ImagemProdutoResponse> imagens,
        List<ProdutoAtributoValorResponse> atributos,
        List<FornecedorProdutoResponse> fornecedores,
        List<ProdutoVariacaoResponse> variacoes
) {}
