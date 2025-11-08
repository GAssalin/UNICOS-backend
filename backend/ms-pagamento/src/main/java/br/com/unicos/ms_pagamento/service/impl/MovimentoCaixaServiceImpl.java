package br.com.unicos.ms_pagamento.service.impl;

import br.com.unicos.ms_pagamento.dto.MovimentoCaixaRequest;
import br.com.unicos.ms_pagamento.dto.MovimentoCaixaResponse;
import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;
import br.com.unicos.ms_pagamento.model.caixa.MovimentoCaixa;
import br.com.unicos.ms_pagamento.model.core.Pagamento;
import br.com.unicos.ms_pagamento.repository.ContaFinanceiraRepository;
import br.com.unicos.ms_pagamento.repository.MovimentoCaixaRepository;
import br.com.unicos.ms_pagamento.repository.PagamentoRepository;
import br.com.unicos.ms_pagamento.service.MovimentoCaixaService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface MovimentoCaixaService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de movimentos de caixa.
 */
@Service
@Transactional
public class MovimentoCaixaServiceImpl implements MovimentoCaixaService {

    private final MovimentoCaixaRepository movimentoCaixaRepository;
    private final ContaFinanceiraRepository contaFinanceiraRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper mapper;

    public MovimentoCaixaServiceImpl(MovimentoCaixaRepository movimentoCaixaRepository,
                                     ContaFinanceiraRepository contaFinanceiraRepository,
                                     PagamentoRepository pagamentoRepository,
                                     ModelMapper mapper) {
        this.movimentoCaixaRepository = movimentoCaixaRepository;
        this.contaFinanceiraRepository = contaFinanceiraRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public MovimentoCaixaResponse salvar(MovimentoCaixaRequest request) {
        ContaFinanceira conta = contaFinanceiraRepository.findById(request.contaFinanceiraId())
                .orElseThrow(() -> new IllegalArgumentException("Conta financeira não encontrada."));

        Pagamento pagamento = null;
        if (request.pagamentoId() != null) {
            pagamento = pagamentoRepository.findById(request.pagamentoId())
                    .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));
        }

        MovimentoCaixa movimento = mapper.map(request, MovimentoCaixa.class);
        movimento.setContaFinanceira(conta);
        movimento.setPagamento(pagamento);

        MovimentoCaixa salvo = movimentoCaixaRepository.save(movimento);
        return toResponse(salvo);
    }

    @Override
    public MovimentoCaixaResponse atualizar(Long id, MovimentoCaixaRequest request) {
        MovimentoCaixa existente = movimentoCaixaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimento de caixa não encontrado com ID: " + id));

        ContaFinanceira conta = contaFinanceiraRepository.findById(request.contaFinanceiraId())
                .orElseThrow(() -> new IllegalArgumentException("Conta financeira não encontrada."));

        Pagamento pagamento = null;
        if (request.pagamentoId() != null) {
            pagamento = pagamentoRepository.findById(request.pagamentoId())
                    .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));
        }

        existente.setDataMovimento(request.dataMovimento());
        existente.setValor(request.valor());
        existente.setTipoMovimento(request.tipoMovimento());
        existente.setDescricao(request.descricao());
        existente.setContaFinanceira(conta);
        existente.setPagamento(pagamento);

        MovimentoCaixa atualizado = movimentoCaixaRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public Optional<MovimentoCaixaResponse> buscarPorId(Long id) {
        return movimentoCaixaRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<MovimentoCaixaResponse> listarTodos() {
        return movimentoCaixaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        movimentoCaixaRepository.deleteById(id);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    public List<MovimentoCaixaResponse> listarPorTipo(TipoMovimentoCaixa tipoMovimento) {
        return movimentoCaixaRepository.findByTipoMovimento(tipoMovimento)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<MovimentoCaixaResponse> listarPorData(LocalDate dataMovimento) {
        return movimentoCaixaRepository.findByDataMovimento(dataMovimento)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 💼 FINANCEIRO
    // ==================================

    @Override
    public BigDecimal calcularTotalEntradas() {
        return movimentoCaixaRepository.calcularTotalEntradas() != null
                ? movimentoCaixaRepository.calcularTotalEntradas()
                : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularTotalSaidas() {
        return movimentoCaixaRepository.calcularTotalSaidas() != null
                ? movimentoCaixaRepository.calcularTotalSaidas()
                : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal totalEntradasHoje() {
        return movimentoCaixaRepository.totalEntradasHoje() != null
                ? movimentoCaixaRepository.totalEntradasHoje()
                : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal totalSaidasHoje() {
        return movimentoCaixaRepository.totalSaidasHoje() != null
                ? movimentoCaixaRepository.totalSaidasHoje()
                : BigDecimal.ZERO;
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private MovimentoCaixaResponse toResponse(MovimentoCaixa movimento) {
        return mapper.map(movimento, MovimentoCaixaResponse.class);
    }
}
