package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.mapper.HistoricoPrecoMapper;
import br.com.unicos.ms_produtos.model.HistoricoPreco;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.HistoricoPrecoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.HistoricoPrecoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Produto não encontrado com ID: " + produtoId));

        HistoricoPreco entity = HistoricoPreco.builder()
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
    public Optional<HistoricoPrecoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Override
    public List<HistoricoPrecoResponse> listarTodos() {
        return repository.findAllByOrderByDataAlteracaoDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<HistoricoPrecoResponse> listarPorProduto(Long produtoId) {
        return repository.findByProdutoIdOrderByDataAlteracaoDesc(produtoId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<HistoricoPrecoListDTO> listarUltimosPorProduto(Long produtoId) {
        return repository.findTop10ByProdutoIdOrderByDataAlteracaoDesc(produtoId)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // REMOÇÃO
    // ============================================================

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Histórico de preço não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
