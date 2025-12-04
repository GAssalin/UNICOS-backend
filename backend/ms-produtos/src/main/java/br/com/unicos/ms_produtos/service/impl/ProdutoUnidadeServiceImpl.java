package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeRequest;
import br.com.unicos.ms_produtos.dto.produto_unidade.ProdutoUnidadeResponse;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.model.ProdutoUnidade;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.repository.ProdutoUnidadeRepository;
import br.com.unicos.ms_produtos.repository.UnidadeMedidaRepository;
import br.com.unicos.ms_produtos.service.ProdutoUnidadeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link ProdutoUnidadeService}.
 * <p>
 * Responsável por aplicar as regras de negócio relacionadas
 * ao vínculo entre produtos e unidades de medida.
 */
@Service
@RequiredArgsConstructor
public class ProdutoUnidadeServiceImpl implements ProdutoUnidadeService {

    private final ProdutoUnidadeRepository produtoUnidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    @Transactional
    public ProdutoUnidadeResponse salvar(ProdutoUnidadeRequest request) {

        Produto produto = produtoRepository.findById(request.unidadeMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        ProdutoUnidade novo = new ProdutoUnidade();
        novo.setProduto(produto);

        UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade Medida não encontrada."));
        novo.setUnidadeMedida(unidadeMedida);

        novo.setQuantidadePadrao(request.quantidadePadrao());
        novo.setFatorConversao(request.fatorConversao());

        ProdutoUnidade salvo = produtoUnidadeRepository.save(novo);

        return toResponse(salvo);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    @Transactional
    public ProdutoUnidadeResponse atualizar(Long id, ProdutoUnidadeRequest request) {

        ProdutoUnidade entidade = produtoUnidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo produto–unidade não encontrado."));

        entidade.getUnidadeMedida().setId(request.unidadeMedidaId());
        entidade.setQuantidadePadrao(request.quantidadePadrao());
        entidade.setFatorConversao(request.fatorConversao());

        ProdutoUnidade atualizado = produtoUnidadeRepository.save(entidade);

        return toResponse(atualizado);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<ProdutoUnidadeResponse> buscarPorId(Long id) {
        return produtoUnidadeRepository.findById(id)
                .map(this::toResponse);
    }

    // ============================================================
    // Listar todos
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoUnidadeResponse> listarTodos() {
        return produtoUnidadeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // Listar por produto
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoUnidadeResponse> listarPorProduto(Long produtoId) {
        return produtoUnidadeRepository.findByProdutoId(produtoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // Listar por unidade de medida
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoUnidadeResponse> listarPorUnidadeMedida(Long unidadeMedidaId) {
        return produtoUnidadeRepository.findByUnidadeMedidaId(unidadeMedidaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // Verificar vínculo
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public boolean verificarVinculo(Long produtoId, Long unidadeMedidaId) {
        return produtoUnidadeRepository
                .findByProdutoIdAndUnidadeMedidaId(produtoId, unidadeMedidaId)
                .isPresent();
    }

    // ============================================================
    // Deletar
    // ============================================================

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!produtoUnidadeRepository.existsById(id)) {
            throw new EntityNotFoundException("Vínculo produto–unidade não encontrado para exclusão.");
        }
        produtoUnidadeRepository.deleteById(id);
    }

    // ============================================================
    // Conversão para DTO
    // ============================================================

    private ProdutoUnidadeResponse toResponse(ProdutoUnidade entidade) {
        return new ProdutoUnidadeResponse(
                entidade.getId(),
                entidade.getProduto().getId(),
                entidade.getProduto().getDadosBasicos().getNome(),
                entidade.getUnidadeMedida().getId(),
                null, // unidadeMedidaNome → vem do ms-empresa, via integração
                entidade.getQuantidadePadrao()
        );
    }
}
