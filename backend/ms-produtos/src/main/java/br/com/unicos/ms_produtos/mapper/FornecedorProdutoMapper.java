package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.model.FornecedorProduto;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão de FornecedorProduto.
 *
 * <p>
 * O nome do fornecedor será resolvido futuramente via ms-pessoas.
 * </p>
 */
@Component
public class FornecedorProdutoMapper {

    /**
     * Converte para DTO de resposta detalhado.
     */
    public FornecedorProdutoResponse toResponse(FornecedorProduto entity) {
        return new FornecedorProdutoResponse(
                entity.getId(),
                entity.getFornecedorId(),
                null, // fornecedorNome (resolvido via ms-pessoas futuramente)
                entity.getCodigoFornecedor(),
                entity.getPrecoCusto(),
                entity.getPrazoEntregaDias(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }

    /**
     * Converte para DTO de listagem.
     */
    public FornecedorProdutoListDTO toListDTO(FornecedorProduto entity) {
        return new FornecedorProdutoListDTO(
                entity.getId(),
                entity.getFornecedorId(),
                null, // fornecedorNome (lookup externo)
                entity.getCodigoFornecedor(),
                entity.getPrecoCusto(),
                entity.getPrazoEntregaDias()
        );
    }
}
