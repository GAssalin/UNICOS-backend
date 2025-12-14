package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeRequest;
import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeResponse;
import br.com.unicos.ms_produtos.mapper.ProdutoUnidadeMapper;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoUnidade;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoUnidadeRepository;
import br.com.unicos.ms_produtos.repository.UnidadeMedidaRepository;
import br.com.unicos.ms_produtos.service.interfaces.ProdutoUnidadeService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoUnidadeServiceImpl implements ProdutoUnidadeService {

    private final ProdutoUnidadeRepository produtoUnidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final ProdutoUnidadeMapper produtoUnidadeMapper;

    // ============================================================
    // CRIAR
    // ============================================================

    @Override
    @Transactional
    public ProdutoUnidadeResponse salvar(Long produtoId, ProdutoUnidadeRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = produtoRepository
                .findByEmpresaIdAndId(empresaId, produtoId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Produto não encontrado.")
                );

        UnidadeMedida unidadeMedida = unidadeMedidaRepository
                .findById(request.unidadeMedidaId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Unidade de medida não encontrada.")
                );

        if (produtoUnidadeRepository.existsByEmpresaIdAndProdutoIdAndUnidadeMedidaId(
                empresaId,
                produto.getId(),
                unidadeMedida.getId()
        )) {
            throw new IllegalArgumentException(
                    "Este vínculo já existe para o produto."
            );
        }

        ProdutoUnidade entity = produtoUnidadeMapper.toEntity(
                request,
                produto,
                unidadeMedida,
                empresaId
        );

        return produtoUnidadeMapper.toResponse(
                produtoUnidadeRepository.save(entity)
        );
    }


    // ============================================================
    // ATUALIZAR
    // ============================================================

    @Override
    @Transactional
    public ProdutoUnidadeResponse atualizar(Long id, ProdutoUnidadeRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        ProdutoUnidade entity = produtoUnidadeRepository
                .findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vínculo produto–unidade não encontrado.")
                );

        UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada."));

        entity.setUnidadeMedida(unidadeMedida);
        entity.setQuantidadePadrao(request.quantidadePadrao());
        entity.setFatorConversao(
                request.fatorConversao() != null ? request.fatorConversao() : 1.0
        );

        return produtoUnidadeMapper.toResponse(
                produtoUnidadeRepository.save(entity)
        );
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<ProdutoUnidadeResponse> buscarPorId(Long id) {
        return produtoUnidadeRepository
                .findByEmpresaIdAndId(TenantContext.getEmpresaId(), id)
                .map(produtoUnidadeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoUnidadeResponse> listarTodos() {
        return produtoUnidadeRepository
                .findByEmpresaId(TenantContext.getEmpresaId())
                .stream()
                .map(produtoUnidadeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoUnidadeResponse> listarPorProduto(Long produtoId) {
        return produtoUnidadeRepository
                .findByEmpresaIdAndProdutoId(
                        TenantContext.getEmpresaId(),
                        produtoId
                )
                .stream()
                .map(produtoUnidadeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoUnidadeResponse> listarPorUnidadeMedida(Long unidadeMedidaId) {
        return produtoUnidadeRepository
                .findByEmpresaIdAndUnidadeMedidaId(
                        TenantContext.getEmpresaId(),
                        unidadeMedidaId
                )
                .stream()
                .map(produtoUnidadeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarVinculo(Long produtoId, Long unidadeMedidaId) {
        return produtoUnidadeRepository
                .existsByEmpresaIdAndProdutoIdAndUnidadeMedidaId(
                        TenantContext.getEmpresaId(),
                        produtoId,
                        unidadeMedidaId
                );
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Override
    @Transactional
    public void deletar(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        if (!produtoUnidadeRepository.existsByEmpresaIdAndId(empresaId, id)) {
            throw new EntityNotFoundException("Vínculo produto–unidade não encontrado.");
        }

        produtoUnidadeRepository.deleteById(id);
    }
}
