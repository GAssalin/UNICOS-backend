package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.ProdutoEstoqueListDTO;
import br.com.unicos.ms_estoque.dto.ProdutoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.ProdutoEstoqueResponse;
import br.com.unicos.ms_estoque.model.EstoqueLocal;
import br.com.unicos.ms_estoque.model.ProdutoEstoque;
import br.com.unicos.ms_estoque.repository.EstoqueLocalRepository;
import br.com.unicos.ms_estoque.repository.ProdutoEstoqueRepository;
import br.com.unicos.ms_estoque.service.ProdutoEstoqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação da interface {@link ProdutoEstoqueService}.
 * <p>
 * Responsável pelas regras de negócio relacionadas aos produtos
 * armazenados nos locais de estoque, incluindo controle de saldo,
 * quantidade mínima e máxima.
 */
@Service
@RequiredArgsConstructor
public class ProdutoEstoqueServiceImpl implements ProdutoEstoqueService {

    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final EstoqueLocalRepository estoqueLocalRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ProdutoEstoqueResponse salvar(ProdutoEstoqueRequest request) {
        EstoqueLocal estoqueLocal = estoqueLocalRepository.findById(request.estoqueLocalId())
                .orElseThrow(() -> new EntityNotFoundException("Local de estoque não encontrado."));

        // Verifica duplicidade: o mesmo produto no mesmo local
        boolean existente = produtoEstoqueRepository
                .findByProdutoIdAndEstoqueLocalId(request.produtoId(), estoqueLocal.getId())
                .isPresent();

        if (existente) {
            throw new DataIntegrityViolationException("Produto já cadastrado neste local de estoque.");
        }

        ProdutoEstoque produtoEstoque = ProdutoEstoque.builder()
                .produtoId(request.produtoId())
                .estoqueLocal(estoqueLocal)
                .quantidade(request.quantidade())
                .quantidadeMinima(request.quantidadeMinima() != null ? request.quantidadeMinima() : 0.0)
                .quantidadeMaxima(request.quantidadeMaxima() != null ? request.quantidadeMaxima() : 0.0)
                .build();

        produtoEstoqueRepository.save(produtoEstoque);
        return modelMapper.map(produtoEstoque, ProdutoEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ProdutoEstoqueResponse atualizar(Long id, ProdutoEstoqueRequest request) {
        ProdutoEstoque produtoEstoque = produtoEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto em estoque não encontrado."));

        if (request.quantidade() != null) {
            produtoEstoque.setQuantidade(request.quantidade());
        }

        if (request.quantidadeMinima() != null) {
            produtoEstoque.setQuantidadeMinima(request.quantidadeMinima());
        }

        if (request.quantidadeMaxima() != null) {
            produtoEstoque.setQuantidadeMaxima(request.quantidadeMaxima());
        }

        produtoEstoqueRepository.save(produtoEstoque);
        return modelMapper.map(produtoEstoque, ProdutoEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        if (!produtoEstoqueRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto em estoque não encontrado.");
        }

        try {
            produtoEstoqueRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível excluir: o produto possui movimentações vinculadas.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEstoqueListDTO> listarTodos() {
        return produtoEstoqueRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, ProdutoEstoqueListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ProdutoEstoqueResponse buscarPorId(Long id) {
        ProdutoEstoque produto = produtoEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto em estoque não encontrado."));
        return modelMapper.map(produto, ProdutoEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEstoqueListDTO> listarPorEstoque(Long estoqueLocalId) {
        return produtoEstoqueRepository.findByEstoqueLocalId(estoqueLocalId).stream()
                .map(entity -> modelMapper.map(entity, ProdutoEstoqueListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEstoqueListDTO> listarEstoqueBaixo() {
        return produtoEstoqueRepository.findProdutosComEstoqueBaixo().stream()
                .map(entity -> modelMapper.map(entity, ProdutoEstoqueListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEstoqueListDTO> listarEstoqueExcedente() {
        return produtoEstoqueRepository.findProdutosComEstoqueExcedente().stream()
                .map(entity -> modelMapper.map(entity, ProdutoEstoqueListDTO.class))
                .toList();
    }
}
