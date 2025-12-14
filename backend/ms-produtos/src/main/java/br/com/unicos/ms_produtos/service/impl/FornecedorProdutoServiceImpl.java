package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.mapper.FornecedorProdutoMapper;
import br.com.unicos.ms_produtos.model.FornecedorProduto;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.FornecedorProdutoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.interfaces.FornecedorProdutoService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
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

        Long empresaId = TenantContext.getEmpresaId();

        if (repository.existsByEmpresaIdAndFornecedorIdAndProdutoId(
                empresaId,
                request.fornecedorId(),
                request.produtoId()
        )) {
            throw new IllegalArgumentException(
                    "Já existe um vínculo entre este fornecedor e produto para esta empresa."
            );
        }

        Produto produto = produtoRepository.findByEmpresaIdAndId(
                        empresaId,
                        request.produtoId()
                )
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        FornecedorProduto novo = FornecedorProduto.builder()
                .empresaId(empresaId)
                .fornecedorId(request.fornecedorId())
                .produto(produto)
                .codigoFornecedor(request.codigoFornecedor())
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

        Long empresaId = TenantContext.getEmpresaId();

        FornecedorProduto existente = repository.findById(id)
                .filter(fp -> fp.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vínculo não encontrado para esta empresa."
                ));

        Produto produto = produtoRepository.findByEmpresaIdAndId(
                        empresaId,
                        request.produtoId()
                )
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        existente.setFornecedorId(request.fornecedorId());
        existente.setProduto(produto);
        existente.setCodigoFornecedor(request.codigoFornecedor());
        existente.setPrecoCusto(request.precoCusto());
        existente.setPrazoEntregaDias(request.prazoEntregaDias());

        return mapper.toResponse(repository.save(existente));
    }

    // ============================================================
    // DELETAR
    // ============================================================

    @Override
    public void deletar(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        FornecedorProduto existente = repository.findById(id)
                .filter(fp -> fp.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vínculo não encontrado para esta empresa."
                ));

        repository.delete(existente);
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    @Override
    public Optional<FornecedorProdutoResponse> buscarPorId(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findById(id)
                .filter(fp -> fp.getEmpresaId().equals(empresaId))
                .map(mapper::toResponse);
    }

    @Override
    public List<FornecedorProdutoResponse> listarTodos() {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndFornecedorId(
                        empresaId,
                        null
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<FornecedorProdutoListDTO> listarPorProduto(Long produtoId) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndProdutoId(empresaId, produtoId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Override
    public List<FornecedorProdutoListDTO> listarPorFornecedor(Long fornecedorId) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndFornecedorId(empresaId, fornecedorId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // OPERAÇÃO ESPECÍFICA
    // ============================================================

    @Override
    public FornecedorProdutoResponse atualizarPrecoCusto(Long id, BigDecimal novoPrecoCusto) {

        Long empresaId = TenantContext.getEmpresaId();

        FornecedorProduto existente = repository.findById(id)
                .filter(fp -> fp.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vínculo não encontrado para esta empresa."
                ));

        existente.setPrecoCusto(novoPrecoCusto);

        return mapper.toResponse(repository.save(existente));
    }

    // ============================================================
    // VALIDAÇÃO
    // ============================================================

    @Override
    public boolean existeVinculo(Long fornecedorId, Long produtoId) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.existsByEmpresaIdAndFornecedorIdAndProdutoId(
                empresaId,
                fornecedorId,
                produtoId
        );
    }
}
