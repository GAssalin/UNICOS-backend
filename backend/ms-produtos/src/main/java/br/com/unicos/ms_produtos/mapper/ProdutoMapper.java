package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produtos.model.Produto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper responsável pela conversão entre Produto
 * e seus respectivos DTOs.
 */
@Component
public class ProdutoMapper {
    private final ProdutoVariacaoMapper produtoVariacaoMapper;
    private final ProdutoAtributoValorMapper produtoAtributoValorMapper;
    private final ImagemProdutoMapper imagemProdutoMapper;
    private final FornecedorProdutoMapper fornecedorProdutoMapper;
    private final CategoriaMapper categoriaMapper;
    private final MarcaMapper marcaMapper;

    public ProdutoMapper(ProdutoVariacaoMapper produtoVariacaoMapper, ProdutoAtributoValorMapper produtoAtributoValorMapper, ImagemProdutoMapper imagemProdutoMapper, FornecedorProdutoMapper fornecedorProdutoMapper, CategoriaMapper categoriaMapper, MarcaMapper marcaMapper) {
        this.produtoVariacaoMapper = produtoVariacaoMapper;
        this.produtoAtributoValorMapper = produtoAtributoValorMapper;
        this.imagemProdutoMapper = imagemProdutoMapper;
        this.fornecedorProdutoMapper = fornecedorProdutoMapper;
        this.categoriaMapper = categoriaMapper;
        this.marcaMapper = marcaMapper;
    }

    public ProdutoResponse toResponse(Produto produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.getAtivo(),
                produto.getDadosBasicos(),
                produto.getTributacao(),
                produto.getPrecoAtual(),

                produto.getCategoria() != null
                        ? categoriaMapper.toResponse(produto.getCategoria())
                        : null,

                produto.getMarca() != null
                        ? marcaMapper.toResponse(produto.getMarca())
                        : null,

                produto.getImagens() == null
                        ? List.of()
                        : produto.getImagens().stream()
                        .map(imagemProdutoMapper::toResponse)
                        .toList(),

                produto.getAtributos() == null
                        ? List.of()
                        : produto.getAtributos().stream()
                        .map(a -> produtoAtributoValorMapper.toResponse(a, produto.getId()))
                        .toList(),

                produto.getFornecedores() == null
                        ? List.of()
                        : produto.getFornecedores().stream()
                        .map(fornecedorProdutoMapper::toResponse)
                        .toList(),

                produto.getVariacoes() == null
                        ? List.of()
                        : produto.getVariacoes().stream()
                        .map(produtoVariacaoMapper::toResponse)
                        .toList()
        );
    }
}
