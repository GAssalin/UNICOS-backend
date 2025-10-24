package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.FornecedorProduto;
import br.com.erp.ms_produtos.repository.FornecedorProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FornecedorProdutoServiceImpl implements FornecedorProdutoService {

    private final FornecedorProdutoRepository fornecedorProdutoRepository;

    @Override
    @Transactional
    public FornecedorProduto salvar(FornecedorProduto fornecedorProduto) {
        return fornecedorProdutoRepository.save(fornecedorProduto);
    }

    @Override
    @Transactional
    public FornecedorProduto atualizar(Long id, FornecedorProduto fornecedorProduto) {
        FornecedorProduto existente = fornecedorProdutoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FornecedorProduto não encontrado"));
        fornecedorProduto.setId(existente.getId());
        return fornecedorProdutoRepository.save(fornecedorProduto);
    }

    @Override
    public Optional<FornecedorProduto> buscarPorId(Long id) {
        return fornecedorProdutoRepository.findById(id);
    }

    @Override
    public Optional<FornecedorProduto> buscarPorFornecedorEProduto(Long fornecedorId, Long produtoId) {
        return fornecedorProdutoRepository.findByFornecedorIdAndProdutoId(fornecedorId, produtoId);
    }

    @Override
    public List<FornecedorProduto> listarPorProduto(Long produtoId) {
        return fornecedorProdutoRepository.findByProdutoId(produtoId);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        fornecedorProdutoRepository.deleteById(id);
    }
}