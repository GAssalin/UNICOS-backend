package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.MovimentacaoLoteRequest;
import br.com.unicos.ms_estoque.dto.MovimentacaoLoteResponse;
import br.com.unicos.ms_estoque.model.LoteSerie;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoque;
import br.com.unicos.ms_estoque.model.MovimentacaoLote;
import br.com.unicos.ms_estoque.repository.LoteSerieRepository;
import br.com.unicos.ms_estoque.repository.MovimentacaoEstoqueRepository;
import br.com.unicos.ms_estoque.repository.MovimentacaoLoteRepository;
import br.com.unicos.ms_estoque.service.MovimentacaoLoteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação da interface {@link MovimentacaoLoteService}.
 * <p>
 * Responsável por controlar a associação entre movimentações de estoque
 * e lotes de produtos, garantindo a rastreabilidade e consistência dos dados.
 */
@Service
@RequiredArgsConstructor
public class MovimentacaoLoteServiceImpl implements MovimentacaoLoteService {

    private final MovimentacaoLoteRepository movimentacaoLoteRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final LoteSerieRepository loteSerieRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MovimentacaoLoteResponse salvar(MovimentacaoLoteRequest request) {
        MovimentacaoEstoque movimentacao = movimentacaoEstoqueRepository.findById(request.movimentacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Movimentação de estoque não encontrada."));

        LoteSerie lote = loteSerieRepository.findById(request.loteId())
                .orElseThrow(() -> new EntityNotFoundException("Lote de produto não encontrado."));

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new DataIntegrityViolationException("A quantidade informada deve ser maior que zero.");
        }

        MovimentacaoLote entity = MovimentacaoLote.builder()
                .movimentacao(movimentacao)
                .lote(lote)
                .quantidade(request.quantidade())
                .build();

        movimentacaoLoteRepository.save(entity);
        return modelMapper.map(entity, MovimentacaoLoteResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        if (!movimentacaoLoteRepository.existsById(id)) {
            throw new EntityNotFoundException("Movimentação de lote não encontrada.");
        }

        try {
            movimentacaoLoteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível excluir: o lote está vinculado a transações de estoque.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovimentacaoLoteResponse> listarPorMovimentacao(Long movimentacaoId) {
        return movimentacaoLoteRepository.findByMovimentacaoId(movimentacaoId).stream()
                .map(entity -> modelMapper.map(entity, MovimentacaoLoteResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovimentacaoLoteResponse> listarPorLote(Long loteId) {
        return movimentacaoLoteRepository.findByLoteId(loteId).stream()
                .map(entity -> modelMapper.map(entity, MovimentacaoLoteResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Double calcularQuantidadeMovimentadaPorLote(Long loteId) {
        Double total = movimentacaoLoteRepository.calcularQuantidadeMovimentadaPorLote(loteId);
        return total != null ? total : 0.0;
    }
}
