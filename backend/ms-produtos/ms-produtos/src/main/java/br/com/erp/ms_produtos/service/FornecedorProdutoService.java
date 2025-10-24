package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.FornecedorProduto;

import java.util.List;
import java.util.Optional;

public interface FornecedorProdutoService {
    FornecedorProduto salvar(FornecedorProduto fornecedorProduto);

    FornecedorProduto atualizar(Long id, FornecedorProduto fornecedorProduto);

    Optional<FornecedorProduto> buscarPorId(Long id);

    Optional<FornecedorProduto> buscarPorFornecedorEProduto(Long fornecedorId, Long produtoId);

    List<FornecedorProduto> listarPorProduto(Long produtoId);

    void deletar(Long id);
}