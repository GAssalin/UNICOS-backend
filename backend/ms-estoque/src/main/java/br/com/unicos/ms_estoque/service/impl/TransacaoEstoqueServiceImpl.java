package br.com.unicos.ms_estoque.service.impl;

import br.com.unicos.ms_estoque.dto.TransacaoEstoqueListDTO;
import br.com.unicos.ms_estoque.dto.TransacaoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.TransacaoEstoqueResponse;
import br.com.unicos.ms_estoque.enums.TipoTransacao;
import br.com.unicos.ms_estoque.model.TransacaoEstoque;
import br.com.unicos.ms_estoque.repository.TransacaoEstoqueRepository;
import br.com.unicos.ms_estoque.service.TransacaoEstoqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação da interface {@link TransacaoEstoqueService}.
 * <p>
 * Responsável pelas regras de negócio das transações de estoque,
 * incluindo entradas, saídas, transferências e ajustes.
 */
@Service
@RequiredArgsConstructor
public class TransacaoEstoqueServiceImpl implements TransacaoEstoqueService {

    private final TransacaoEstoqueRepository transacaoEstoqueRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransacaoEstoqueResponse salvar(TransacaoEstoqueRequest request) {
        if (request.tipo() == null) {
            throw new DataIntegrityViolationException("O tipo da transação é obrigatório.");
        }

        // Regra adicional: se for ajuste, o subtipo deve estar informado
        if (request.tipo() == TipoTransacao.AJUSTE && request.tipoAjuste() == null) {
            throw new DataIntegrityViolationException("Tipo de ajuste é obrigatório para transações do tipo AJUSTE.");
        }

        TransacaoEstoque transacao = TransacaoEstoque.builder()
                .tipo(request.tipo())
                .tipoAjuste(request.tipoAjuste() != null ? request.tipoAjuste() : null)
                .observacao(request.observacao())
                .usuarioResponsavel(request.usuarioResponsavel())
                .data(request.data() != null ? request.data() : LocalDateTime.now())
                .build();

        transacaoEstoqueRepository.save(transacao);
        return modelMapper.map(transacao, TransacaoEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        TransacaoEstoque transacao = transacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação de estoque não encontrada."));

        try {
            transacaoEstoqueRepository.delete(transacao);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível excluir a transação: há movimentações vinculadas.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransacaoEstoqueListDTO> listarTodas() {
        return transacaoEstoqueRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TransacaoEstoqueListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TransacaoEstoqueResponse buscarPorId(Long id) {
        TransacaoEstoque transacao = transacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação de estoque não encontrada."));
        return modelMapper.map(transacao, TransacaoEstoqueResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransacaoEstoqueListDTO> buscarPorTipo(TipoTransacao tipo) {
        return transacaoEstoqueRepository.findByTipo(tipo).stream()
                .map(t -> modelMapper.map(t, TransacaoEstoqueListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransacaoEstoqueListDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return transacaoEstoqueRepository.findByDataBetween(inicio, fim).stream()
                .map(t -> modelMapper.map(t, TransacaoEstoqueListDTO.class))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarPorTipoEPeriodo(TipoTransacao tipo, LocalDateTime inicio, LocalDateTime fim) {
        return transacaoEstoqueRepository.countByTipoAndPeriodo(tipo, inicio, fim);
    }
}
