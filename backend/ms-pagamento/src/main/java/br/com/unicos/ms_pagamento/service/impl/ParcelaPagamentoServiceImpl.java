package br.com.unicos.ms_pagamento.service.impl;

import br.com.unicos.ms_pagamento.dto.ParcelaPagamentoRequest;
import br.com.unicos.ms_pagamento.dto.ParcelaPagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusParcela;
import br.com.unicos.ms_pagamento.model.core.ParcelaPagamento;
import br.com.unicos.ms_pagamento.model.core.Pagamento;
import br.com.unicos.ms_pagamento.repository.ParcelaPagamentoRepository;
import br.com.unicos.ms_pagamento.repository.PagamentoRepository;
import br.com.unicos.ms_pagamento.service.ParcelaPagamentoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface ParcelaPagamentoService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de parcelas de pagamento.
 */
@Service
@Transactional
public class ParcelaPagamentoServiceImpl implements ParcelaPagamentoService {

    private final ParcelaPagamentoRepository parcelaPagamentoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper mapper;

    public ParcelaPagamentoServiceImpl(ParcelaPagamentoRepository parcelaPagamentoRepository,
                                       PagamentoRepository pagamentoRepository,
                                       ModelMapper mapper) {
        this.parcelaPagamentoRepository = parcelaPagamentoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public ParcelaPagamentoResponse salvar(ParcelaPagamentoRequest request) {
        Pagamento pagamento = pagamentoRepository.findById(request.pagamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        ParcelaPagamento parcela = mapper.map(request, ParcelaPagamento.class);
        parcela.setPagamento(pagamento);

        ParcelaPagamento salva = parcelaPagamentoRepository.save(parcela);
        return toResponse(salva);
    }

    @Override
    public ParcelaPagamentoResponse atualizar(Long id, ParcelaPagamentoRequest request) {
        ParcelaPagamento existente = parcelaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parcela não encontrada com ID: " + id));

        Pagamento pagamento = pagamentoRepository.findById(request.pagamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        existente.setNumeroParcela(request.numeroParcela());
        existente.setValor(request.valor());
        existente.setDataVencimento(request.dataVencimento());
        existente.setDataPagamento(request.dataPagamento());
        existente.setStatus(request.status());
        existente.setPagamento(pagamento);

        ParcelaPagamento atualizada = parcelaPagamentoRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    public Optional<ParcelaPagamentoResponse> buscarPorId(Long id) {
        return parcelaPagamentoRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<ParcelaPagamentoResponse> listarTodas() {
        return parcelaPagamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        parcelaPagamentoRepository.deleteById(id);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    public List<ParcelaPagamentoResponse> listarPorPagamento(Long pagamentoId) {
        return parcelaPagamentoRepository.findByPagamentoId(pagamentoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ParcelaPagamentoResponse> listarPorStatus(StatusParcela status) {
        return parcelaPagamentoRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ParcelaPagamentoResponse> listarVencidasAntesDe(LocalDate data) {
        return parcelaPagamentoRepository.findByDataVencimentoBefore(data)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ParcelaPagamentoResponse> listarAtrasadas() {
        return parcelaPagamentoRepository.buscarParcelasAtrasadas()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 💼 NEGÓCIO
    // ==================================

    @Override
    public ParcelaPagamentoResponse quitarParcela(Long id) {
        ParcelaPagamento parcela = parcelaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parcela não encontrada."));

        parcela.setStatus(StatusParcela.QUITADA);
        parcela.setDataPagamento(LocalDate.now());
        return toResponse(parcelaPagamentoRepository.save(parcela));
    }

    @Override
    public ParcelaPagamentoResponse atualizarStatus(Long id, StatusParcela status) {
        ParcelaPagamento parcela = parcelaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parcela não encontrada."));

        parcela.setStatus(status);
        return toResponse(parcelaPagamentoRepository.save(parcela));
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private ParcelaPagamentoResponse toResponse(ParcelaPagamento parcela) {
        return mapper.map(parcela, ParcelaPagamentoResponse.class);
    }
}
