package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.model.HistoricoPreco;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.HistoricoPrecoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.HistoricoPrecoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface HistoricoPrecoService.
 * Responsável pela lógica de negócio e manipulação dos registros
 * de alterações de preço de produtos.
 */
@Service
@Transactional
public class HistoricoPrecoServiceImpl implements HistoricoPrecoService {

    private final HistoricoPrecoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ModelMapper mapper;

    public HistoricoPrecoServiceImpl(HistoricoPrecoRepository repository,
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
    public HistoricoPrecoResponse salvar(HistoricoPrecoRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + request.produtoId()));

        HistoricoPreco historico = HistoricoPreco.builder()
                .produto(produto)
                .precoAnterior(request.precoAnterior())
                .novoPreco(request.novoPreco())
                .motivo(request.motivo())
                .build();

        HistoricoPreco salvo = repository.save(historico);
        return toResponse(salvo);
    }

    @Override
    public Optional<HistoricoPrecoResponse> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Override
    public List<HistoricoPrecoResponse> listarTodos() {
        return repository.findAllByOrderByDataAlteracaoDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<HistoricoPrecoResponse> listarPorProduto(Long produtoId) {
        return repository.findByProdutoIdOrderByDataAlteracaoDesc(produtoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<HistoricoPrecoListDTO> listarUltimosPorProduto(Long produtoId) {
        return repository.findTop10ByProdutoIdOrderByDataAlteracaoDesc(produtoId)
                .stream()
                .map(h -> new HistoricoPrecoListDTO(
                        h.getId(),
                        h.getPrecoAnterior(),
                        h.getNovoPreco(),
                        h.getDataAlteracao()
                ))
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Histórico de preço não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODOS AUXILIARES
    // ==================================

    private HistoricoPrecoResponse toResponse(HistoricoPreco entity) {
        return new HistoricoPrecoResponse(
                entity.getId(),
                entity.getProduto().getId(),
                entity.getProduto().getNome(),
                entity.getPrecoAnterior(),
                entity.getNovoPreco(),
                entity.getDataAlteracao(),
                entity.getMotivo()
        );
    }
}