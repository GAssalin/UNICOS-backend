package br.com.erp.ms_produtos.service.impl;

import br.com.erp.ms_produtos.dto.FornecedorProdutoListDTO;
import br.com.erp.ms_produtos.dto.FornecedorProdutoRequest;
import br.com.erp.ms_produtos.dto.FornecedorProdutoResponse;
import br.com.erp.ms_produtos.model.FornecedorProduto;
import br.com.erp.ms_produtos.model.Produto;
import br.com.erp.ms_produtos.repository.FornecedorProdutoRepository;
import br.com.erp.ms_produtos.repository.ProdutoRepository;
import br.com.erp.ms_produtos.service.FornecedorProdutoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface FornecedorProdutoService.
 * Responsável pela lógica de negócio e manipulação dos vínculos
 * entre fornecedores e produtos.
 */
@Service
@Transactional
public class FornecedorProdutoServiceImpl implements FornecedorProdutoService {

    private final FornecedorProdutoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ModelMapper mapper;

    public FornecedorProdutoServiceImpl(FornecedorProdutoRepository repository,
                                        ProdutoRepository produtoRepository,
                                        ModelMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public FornecedorProdutoResponse salvar(FornecedorProdutoRequest request) {
        if (repository.existsByFornecedorIdAndProdutoId(request.getFornecedorId(), request.getProdutoId())) {
            throw new IllegalArgumentException("Já existe um vínculo entre esse fornecedor e produto.");
        }

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        FornecedorProduto entidade = FornecedorProduto.builder()
                .fornecedorId(request.getFornecedorId())
                .produto(produto)
                .precoCusto(request.getPrecoCusto())
                .prazoEntregaDias(request.getPrazoEntregaDias())
                .build();

        FornecedorProduto salvo = repository.save(entidade);
        return toResponse(salvo);
    }

    @Override
    public FornecedorProdutoResponse atualizar(Long id, FornecedorProdutoRequest request) {
        FornecedorProduto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado com ID: " + id));

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        existente.setFornecedorId(request.getFornecedorId());
        existente.setProduto(produto);
        existente.setPrecoCusto(request.getPrecoCusto());
        existente.setPrazoEntregaDias(request.getPrazoEntregaDias());

        FornecedorProduto atualizado = repository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public Optional<FornecedorProdutoResponse> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Override
    public List<FornecedorProdutoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<FornecedorProdutoListDTO> listarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId)
                .stream()
                .map(fp -> FornecedorProdutoListDTO.builder()
                        .id(fp.getId())
                        .fornecedorId(fp.getFornecedorId())
                        .produtoNome(fp.getProduto().getNome())
                        .precoCusto(fp.getPrecoCusto())
                        .build())
                .toList();
    }

    @Override
    public List<FornecedorProdutoListDTO> listarPorFornecedor(Long fornecedorId) {
        return repository.findByFornecedorId(fornecedorId)
                .stream()
                .map(fp -> FornecedorProdutoListDTO.builder()
                        .id(fp.getId())
                        .fornecedorId(fp.getFornecedorId())
                        .produtoNome(fp.getProduto().getNome())
                        .precoCusto(fp.getPrecoCusto())
                        .build())
                .toList();
    }

    @Override
    public FornecedorProdutoResponse atualizarPrecoCusto(Long id, BigDecimal novoPrecoCusto) {
        FornecedorProduto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado com ID: " + id));

        existente.setPrecoCusto(novoPrecoCusto);
        FornecedorProduto atualizado = repository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Vínculo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existeVinculo(Long fornecedorId, Long produtoId) {
        return repository.existsByFornecedorIdAndProdutoId(fornecedorId, produtoId);
    }

    // ==================================
    // 🧭 MÉTODOS AUXILIARES
    // ==================================

    private FornecedorProdutoResponse toResponse(FornecedorProduto entity) {
        return FornecedorProdutoResponse.builder()
                .id(entity.getId())
                .fornecedorId(entity.getFornecedorId())
                .produtoId(entity.getProduto().getId())
                .produtoNome(entity.getProduto().getNome())
                .precoCusto(entity.getPrecoCusto())
                .prazoEntregaDias(entity.getPrazoEntregaDias())
                .build();
    }
}