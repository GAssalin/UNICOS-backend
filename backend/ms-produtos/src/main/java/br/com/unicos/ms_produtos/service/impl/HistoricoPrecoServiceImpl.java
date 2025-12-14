package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.mapper.HistoricoPrecoMapper;
import br.com.unicos.ms_produtos.model.HistoricoPreco;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.HistoricoPrecoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.interfaces.HistoricoPrecoService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por registrar e consultar alterações de preço de produtos.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class HistoricoPrecoServiceImpl implements HistoricoPrecoService {

    private final HistoricoPrecoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final HistoricoPrecoMapper mapper;

    // ============================================================
    // CRIAÇÃO DE REGISTRO
    // ============================================================

    @Override
    public HistoricoPrecoResponse salvar(Long produtoId, HistoricoPrecoRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = produtoRepository.findByEmpresaIdAndId(empresaId, produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado para a empresa atual"
                        )
                );

        HistoricoPreco entity = HistoricoPreco.builder()
                .empresaId(empresaId)
                .produto(produto)
                .precoAnterior(request.precoAnterior())
                .novoPreco(request.novoPreco())
                .motivo(request.motivo())
                .build();

        return mapper.toResponse(repository.save(entity));
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoricoPrecoResponse> buscarPorId(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdAndId(empresaId, id)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoPrecoResponse> listarTodos() {

        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaIdOrderByDataAlteracaoDesc(empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoPrecoResponse> listarPorProduto(Long produtoId) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository
                .findByEmpresaIdAndProdutoIdOrderByDataAlteracaoDesc(
                        empresaId,
                        produtoId
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoPrecoListDTO> listarUltimosPorProduto(Long produtoId) {

        Long empresaId = TenantContext.getEmpresaId();

        return repository
                .findTop10ByEmpresaIdAndProdutoIdOrderByDataAlteracaoDesc(
                        empresaId,
                        produtoId
                )
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // REMOÇÃO
    // ============================================================

    @Override
    public void deletar(Long id) {

        Long empresaId = TenantContext.getEmpresaId();

        if (!repository.existsByEmpresaIdAndId(empresaId, id)) {
            throw new IllegalArgumentException(
                    "Histórico de preço não encontrado para a empresa atual"
            );
        }

        repository.deleteById(id);
    }
}
