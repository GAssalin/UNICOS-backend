package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.MovimentacaoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.MovimentacaoEstoqueResponse;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoque;
import br.com.unicos.ms_estoque.model.ProdutoEstoque;
import br.com.unicos.ms_estoque.model.TransacaoEstoque;
import br.com.unicos.ms_estoque.repository.MovimentacaoEstoqueRepository;
import br.com.unicos.ms_estoque.repository.ProdutoEstoqueRepository;
import br.com.unicos.ms_estoque.repository.TransacaoEstoqueRepository;
import br.com.unicos.ms_estoque.service.MovimentacaoEstoqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação da interface {@link MovimentacaoEstoqueService}.
 * <p>
 * Responsável pelas regras de negócio relacionadas à movimentação
 * de produtos dentro das transações de estoque.
 */
@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueServiceImpl implements MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final TransacaoEstoqueRepository transacaoEstoqueRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MovimentacaoEstoqueResponse salvar(MovimentacaoEstoqueRequest request) {
        TransacaoEstoque transacao = transacaoEstoqueRepository.findById(request.transacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Transação de estoque não encontrada."));

        ProdutoEstoque produtoEstoque = produtoEstoqueRepository.findById(request.produtoEstoqueId())
                .orElseThrow(() -> new EntityNotFoundException("Produto em estoque não encontrado."));

        // Regra de validação para quantidade
        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new DataIntegrityViolationException("A quantidade movimentada deve ser maior que zero.");
        }

        // Atualiza saldo do produto conforme o tipo da transação
        double saldoAtual = produtoEstoque.getQuantidade();
        switch (transacao.getTipo()) {
            case ENTRADA -> produtoEstoque.setQuantidade(saldoAtual + request.quantidade());
            case SAIDA, AJUSTE -> produtoEstoque.setQuantidade(saldoAtual - request.quantidade());
            case TRANSFERENCIA -> {
                // Aqui apenas registra; ajuste do saldo pode ser tratado no contexto da transferência
            }
            default -> throw new DataIntegrityViolationException("Tipo de transação inválido para movimentação.");
        }

        // Persiste a movimentação
        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .transacao(transacao)
                .produtoEstoque(produtoEstoque)
                .quantidade(request.quantidade())
                .loteId(request.loteId())
                .build();

        produtoEstoqueRepository.save(produtoEstoque);
        movimentacaoEstoqueRepository.save(movimentacao);

        return modelMapper.map(movimentacao, MovimentacaoEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        MovimentacaoEstoque movimentacao = movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação de estoque não encontrada."));

        try {
            movimentacaoEstoqueRepository.delete(movimentacao);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível excluir: movimentação vinculada a lote ou transação.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueResponse> listarPorTransacao(Long transacaoId) {
        return movimentacaoEstoqueRepository.findByTransacaoId(transacaoId).stream()
                .map(entity -> modelMapper.map(entity, MovimentacaoEstoqueResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueResponse> listarPorProduto(Long produtoEstoqueId) {
        return movimentacaoEstoqueRepository.findByProdutoEstoqueId(produtoEstoqueId).stream()
                .map(entity -> modelMapper.map(entity, MovimentacaoEstoqueResponse.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalMovimentado(Long produtoEstoqueId, LocalDateTime inicio, LocalDateTime fim) {
        Double total = movimentacaoEstoqueRepository.calcularTotalMovimentado(produtoEstoqueId, inicio, fim);
        return total != null ? total : 0.0;
    }
}
