package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.ProdutoUnidadeRequest;
import br.com.unicos.ms_produtos.dto.ProdutoUnidadeResponse;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoUnidade;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoUnidadeRepository;
import br.com.unicos.ms_produtos.repository.UnidadeMedidaRepository;
import br.com.unicos.ms_produtos.service.ProdutoUnidadeService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface ProdutoUnidadeService.
 * Responsável pela lógica de negócio relacionada à criação,
 * atualização e consulta de vínculos entre produtos e unidades de medida.
 */
@Service
@Transactional
public class ProdutoUnidadeServiceImpl implements ProdutoUnidadeService {

    private final ProdutoUnidadeRepository repository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final ModelMapper mapper;

    public ProdutoUnidadeServiceImpl(ProdutoUnidadeRepository repository,
                                     ProdutoRepository produtoRepository,
                                     UnidadeMedidaRepository unidadeMedidaRepository,
                                     ModelMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public ProdutoUnidadeResponse salvar(ProdutoUnidadeRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + request.produtoId()));

        UnidadeMedida unidade = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada com ID: " + request.unidadeMedidaId()));

        // Evita vínculos duplicados
        repository.findByProdutoIdAndUnidadeMedidaId(produto.getId(), unidade.getId())
                .ifPresent(pu -> { throw new IllegalArgumentException("Já existe vínculo entre este produto e unidade de medida."); });

        ProdutoUnidade entity = ProdutoUnidade.builder()
                .produto(produto)
                .unidadeMedida(unidade)
                .quantidadePadrao(request.quantidadePadrao())
                .build();

        ProdutoUnidade salvo = repository.save(entity);
        return toResponse(salvo);
    }

    @Override
    public ProdutoUnidadeResponse atualizar(Long id, ProdutoUnidadeRequest request) {
        ProdutoUnidade existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado com ID: " + id));

        UnidadeMedida unidade = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade de medida não encontrada com ID: " + request.unidadeMedidaId()));

        existente.setUnidadeMedida(unidade);
        existente.setQuantidadePadrao(request.quantidadePadrao());

        ProdutoUnidade atualizado = repository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public Optional<ProdutoUnidadeResponse> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Override
    public List<ProdutoUnidadeResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoUnidadeResponse> listarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoUnidadeResponse> listarPorUnidadeMedida(Long unidadeMedidaId) {
        return repository.findByUnidadeMedidaId(unidadeMedidaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public boolean verificarVinculo(Long produtoId, Long unidadeMedidaId) {
        return repository.findByProdutoIdAndUnidadeMedidaId(produtoId, unidadeMedidaId).isPresent();
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Vínculo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODO AUXILIAR
    // ==================================

    private ProdutoUnidadeResponse toResponse(ProdutoUnidade entity) {
        return new ProdutoUnidadeResponse(
                entity.getId(),
                entity.getProduto().getId(),
                entity.getProduto().getNome(),
                entity.getUnidadeMedida().getId(),
                entity.getUnidadeMedida().getNome(),
                entity.getQuantidadePadrao()
        );
    }
}