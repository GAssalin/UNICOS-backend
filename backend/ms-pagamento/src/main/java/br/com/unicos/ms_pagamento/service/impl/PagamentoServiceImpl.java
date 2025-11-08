package br.com.unicos.ms_pagamento.service.impl;

import br.com.unicos.ms_pagamento.dto.PagamentoRequest;
import br.com.unicos.ms_pagamento.dto.PagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;
import br.com.unicos.ms_pagamento.model.core.Pagamento;
import br.com.unicos.ms_pagamento.repository.FormaPagamentoRepository;
import br.com.unicos.ms_pagamento.repository.PagamentoRepository;
import br.com.unicos.ms_pagamento.service.PagamentoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface PagamentoService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de pagamentos.
 */
@Service
@Transactional
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ModelMapper mapper;

    public PagamentoServiceImpl(PagamentoRepository pagamentoRepository,
                                FormaPagamentoRepository formaPagamentoRepository,
                                ModelMapper mapper) {
        this.pagamentoRepository = pagamentoRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public PagamentoResponse salvar(PagamentoRequest request) {
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(request.formaPagamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Forma de pagamento não encontrada."));

        Pagamento pagamento = mapper.map(request, Pagamento.class);
        pagamento.setFormaPagamento(formaPagamento);

        Pagamento salvo = pagamentoRepository.save(pagamento);
        return toResponse(salvo);
    }

    @Override
    public PagamentoResponse atualizar(Long id, PagamentoRequest request) {
        Pagamento existente = pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + id));

        FormaPagamento formaPagamento = formaPagamentoRepository.findById(request.formaPagamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Forma de pagamento não encontrada."));

        existente.setValor(request.valor());
        existente.setDataVencimento(request.dataVencimento());
        existente.setDataPagamento(request.dataPagamento());
        existente.setStatus(request.status());
        existente.setTipoTransacao(request.tipoTransacao());
        existente.setReferenciaId(request.referenciaId());
        existente.setObservacao(request.observacao());
        existente.setFormaPagamento(formaPagamento);

        Pagamento atualizado = pagamentoRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public Optional<PagamentoResponse> buscarPorId(Long id) {
        return pagamentoRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<PagamentoResponse> listarTodos() {
        return pagamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        pagamentoRepository.deleteById(id);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    public List<PagamentoResponse> listarPorStatus(StatusPagamento status) {
        return pagamentoRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PagamentoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return pagamentoRepository.findByDataPagamentoBetween(inicio, fim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PagamentoResponse> listarPorTipoTransacao(TipoTransacao tipo) {
        return pagamentoRepository.findByTipoTransacao(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PagamentoResponse> listarAtrasados() {
        return pagamentoRepository.findAll()
                .stream()
                .filter(p -> p.getDataVencimento().isBefore(LocalDate.now()) && p.getStatus() != StatusPagamento.PAGO)
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 💼 NEGÓCIO
    // ==================================

    @Override
    public PagamentoResponse atualizarStatus(Long id, StatusPagamento status) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + id));

        pagamento.setStatus(status);
        return toResponse(pagamentoRepository.save(pagamento));
    }

    @Override
    public BigDecimal calcularTotalPago() {
        return pagamentoRepository.totalPago() != null ? pagamentoRepository.totalPago() : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularTotalPendente() {
        return pagamentoRepository.totalPendente() != null ? pagamentoRepository.totalPendente() : BigDecimal.ZERO;
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private PagamentoResponse toResponse(Pagamento pagamento) {
        return mapper.map(pagamento, PagamentoResponse.class);
    }
}
