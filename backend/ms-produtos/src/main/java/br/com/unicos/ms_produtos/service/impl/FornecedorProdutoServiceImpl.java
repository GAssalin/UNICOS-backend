package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.mapper.FornecedorProdutoMapper;
import br.com.unicos.ms_produtos.model.FornecedorProduto;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.FornecedorProdutoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.FornecedorProdutoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelos vínculos entre fornecedores e produtos.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FornecedorProdutoServiceImpl implements FornecedorProdutoService {

    private final FornecedorProdutoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final FornecedorProdutoMapper mapper;

    // ============================================================
    // CRIAR
    // ============================================================

    @Override
    public FornecedorProdutoResponse salvar(FornecedorProdutoRequest request) {

        if (repository.existsByFornecedorIdAndProdutoId(request.fornecedorId(), request.produtoId())) {
            throw new IllegalArgumentException("Já existe um vínculo entre este fornecedor e produto.");
        }

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        FornecedorProduto novo = FornecedorProduto.builder()
                .fornecedorId(request.fornecedorId())
                .codigoFornecedor(request.codigoFornecedor())
                .produto(produto)
                .precoCusto(request.precoCusto())
                .prazoEntregaDias(request.prazoEntregaDias())
                .build();

        return mapper.toResponse(repository.save(novo));
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================

    @Override
    public FornecedorProdutoResponse atualizar(Long id, FornecedorProdutoRequest request) {

        FornecedorProduto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado com ID: " + id));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        existente.setFornecedorId(request.fornecedorId());
        existente.setCodigoFornecedor(request.codigoFornecedor());
        existente.setProduto(produto);
        existente.setPrecoCusto(request.precoCusto());
        existente.setPrazoEntregaDias(request.prazoEntregaDias());

        return mapper.toResponse(repository.save(existente));
    }

    // ============================================================
    // DELETAR
    // ============================================================

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Vínculo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    @Override
    public Optional<FornecedorProdutoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Override
    public List<FornecedorProdutoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<FornecedorProdutoListDTO> listarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Override
    public List<FornecedorProdutoListDTO> listarPorFornecedor(Long fornecedorId) {
        return repository.findByFornecedorId(fornecedorId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // OPERAÇÃO ESPECÍFICA
    // ============================================================

    @Override
    public FornecedorProdutoResponse atualizarPrecoCusto(Long id, BigDecimal novoPrecoCusto) {

        FornecedorProduto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado com ID: " + id));

        existente.setPrecoCusto(novoPrecoCusto);

        return mapper.toResponse(repository.save(existente));
    }

    // ============================================================
    // VALIDAÇÃO
    // ============================================================

    @Override
    public boolean existeVinculo(Long fornecedorId, Long produtoId) {
        return repository.existsByFornecedorIdAndProdutoId(fornecedorId, produtoId);
    }
}
