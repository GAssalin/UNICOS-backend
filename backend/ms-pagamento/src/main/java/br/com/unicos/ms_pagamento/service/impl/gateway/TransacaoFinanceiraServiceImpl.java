package br.com.unicos.ms_pagamento.service.impl.gateway;

import br.com.unicos.ms_pagamento.dto.gateway.TransacaoPagamentoRequest;
import br.com.unicos.ms_pagamento.dto.gateway.TransacaoPagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
import br.com.unicos.ms_pagamento.model.core.Pagamento;
import br.com.unicos.ms_pagamento.model.gateway.TransacaoPagamento;
import br.com.unicos.ms_pagamento.repository.core.PagamentoRepository;
import br.com.unicos.ms_pagamento.service.gateway.TransacaoPagamentoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface TransacaoFinanceiraService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de transações financeiras.
 */
@Service
@Transactional
public class TransacaoFinanceiraServiceImpl implements TransacaoPagamentoService {

    private final TransacaoFinanceiraRepository transacaoFinanceiraRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper mapper;

    public TransacaoFinanceiraServiceImpl(TransacaoFinanceiraRepository transacaoFinanceiraRepository,
                                          PagamentoRepository pagamentoRepository,
                                          ModelMapper mapper) {
        this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public TransacaoPagamentoResponse salvar(TransacaoPagamentoRequest request) {
        Pagamento pagamento = pagamentoRepository.findById(request.pagamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        TransacaoPagamento transacao = mapper.map(request, TransacaoPagamento.class);
        transacao.setPagamento(pagamento);

        TransacaoPagamento salva = transacaoFinanceiraRepository.save(transacao);
        return toResponse(salva);
    }

    @Override
    public TransacaoPagamentoResponse atualizar(Long id, TransacaoPagamentoRequest request) {
        TransacaoPagamento existente = transacaoFinanceiraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada com ID: " + id));

        Pagamento pagamento = pagamentoRepository.findById(request.pagamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        existente.setCodigoTransacao(request.codigoTransacao());
        existente.setDataTransacao(request.dataTransacao());
        existente.setValor(request.valor());
        existente.setStatus(request.status());
        existente.setTipo(request.tipo());
        existente.setPagamento(pagamento);

        TransacaoPagamento atualizada = transacaoFinanceiraRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    public Optional<TransacaoPagamentoResponse> buscarPorId(Long id) {
        return transacaoFinanceiraRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<TransacaoPagamentoResponse> listarTodas() {
        return transacaoFinanceiraRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        transacaoFinanceiraRepository.deleteById(id);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    public Optional<TransacaoPagamentoResponse> buscarPorCodigo(String codigoTransacao) {
        return transacaoFinanceiraRepository.findByCodigoTransacao(codigoTransacao)
                .map(this::toResponse);
    }

    @Override
    public List<TransacaoPagamentoResponse> listarPorStatus(StatusTransacao status) {
        return transacaoFinanceiraRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TransacaoPagamentoResponse> listarPorTipo(TipoFormaPagamento tipo) {
        return transacaoFinanceiraRepository.findByTipo(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TransacaoPagamentoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return transacaoFinanceiraRepository.findByDataTransacaoBetween(inicio, fim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 💼 NEGÓCIO
    // ==================================

    @Override
    public TransacaoPagamentoResponse atualizarStatus(Long id, StatusTransacao status) {
        TransacaoPagamento transacao = transacaoFinanceiraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada."));

        transacao.setStatus(status);
        return toResponse(transacaoFinanceiraRepository.save(transacao));
    }

    @Override
    public Long contarTransacoesFalhas() {
        return transacaoFinanceiraRepository.contarTransacoesFalhas();
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private TransacaoPagamentoResponse toResponse(TransacaoPagamento transacao) {
        return mapper.map(transacao, TransacaoPagamentoResponse.class);
    }
}
