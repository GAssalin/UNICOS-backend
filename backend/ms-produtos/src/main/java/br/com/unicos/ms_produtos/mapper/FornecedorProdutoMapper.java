package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.model.FornecedorProduto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FornecedorProdutoMapper {

    private final ModelMapper mapper;

    /**
     * Converte para o DTO de resposta detalhado.
     */
    public FornecedorProdutoResponse toResponse(FornecedorProduto entity) {
        return new FornecedorProdutoResponse(
                entity.getId(),
                entity.getFornecedorId(),
                null, // fornecedorNome (vem do ms-pessoas futuramente)
                entity.getCodigoFornecedor(),
                entity.getPrecoCusto(),
                entity.getPrazoEntregaDias(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }

    /**
     * Converte para o DTO de listagem.
     */
    public FornecedorProdutoListDTO toListDTO(FornecedorProduto entity) {
        return new FornecedorProdutoListDTO(
                entity.getId(),
                entity.getFornecedorId(),
                null, // fornecedorNome (futuro lookup no ms-pessoas)
                entity.getCodigoFornecedor(),
                entity.getPrecoCusto(),
                entity.getPrazoEntregaDias()
        );
    }
}
