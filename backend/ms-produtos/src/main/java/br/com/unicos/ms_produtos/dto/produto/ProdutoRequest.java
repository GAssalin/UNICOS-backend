package br.com.unicos.ms_produtos.dto.produto;

import br.com.unicos.core.produto.model.PrecoBase;
import br.com.unicos.core.produto.model.ProdutoBase;
import br.com.unicos.core.produto.model.ProdutoTributacaoBase;
import br.com.unicos.ms_produtos.dto.imagem_produto.ImagemProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto_atributo.ProdutoAtributoValorRequest;
import br.com.unicos.ms_produtos.dto.produto_variacao.ProdutoVariacaoRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO utilizado para criação ou atualização de um produto.
 *
 * <p>
 * O contexto da empresa (tenant) é resolvido automaticamente pelo backend.
 * O identificador do produto, quando necessário, deve ser informado na URL.
 * </p>
 */
public record ProdutoRequest(

        @NotNull(message = "Os dados básicos do produto são obrigatórios.")
        ProdutoBase dadosBasicos,

        ProdutoTributacaoBase tributacao,

        @NotNull(message = "As informações de preço são obrigatórias.")
        PrecoBase precoAtual,

        Boolean ativo,

        Long categoriaId,
        Long marcaId,

        List<ImagemProdutoRequest> imagens,
        List<ProdutoAtributoValorRequest> atributos,
        List<ProdutoVariacaoRequest> variacoes
) {}
