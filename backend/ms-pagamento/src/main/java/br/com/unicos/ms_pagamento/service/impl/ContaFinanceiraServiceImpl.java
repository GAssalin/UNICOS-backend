package br.com.unicos.ms_pagamento.service.impl;

import br.com.unicos.ms_pagamento.dto.ContaFinanceiraRequest;
import br.com.unicos.ms_pagamento.dto.ContaFinanceiraResponse;
import br.com.unicos.ms_pagamento.enums.TipoConta;
import br.com.unicos.ms_pagamento.model.ContaFinanceira;
import br.com.unicos.ms_pagamento.repository.ContaFinanceiraRepository;
import br.com.unicos.ms_pagamento.service.ContaFinanceiraService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface ContaFinanceiraService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de contas financeiras.
 */
@Service
@Transactional
public class ContaFinanceiraServiceImpl implements ContaFinanceiraService {

    private final ContaFinanceiraRepository contaFinanceiraRepository;
    private final ModelMapper mapper;

    public ContaFinanceiraServiceImpl(ContaFinanceiraRepository contaFinanceiraRepository, ModelMapper mapper) {
        this.contaFinanceiraRepository = contaFinanceiraRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public ContaFinanceiraResponse salvar(ContaFinanceiraRequest request) {
        ContaFinanceira conta = mapper.map(request, ContaFinanceira.class);
        ContaFinanceira salva = contaFinanceiraRepository.save(conta);
        return toResponse(salva);
    }

    @Override
    public ContaFinanceiraResponse atualizar(Long id, ContaFinanceiraRequest request) {
        ContaFinanceira existente = contaFinanceiraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta financeira não encontrada com ID: " + id));

        existente.setDescricao(request.descricao());
        existente.setBanco(request.banco());
        existente.setAgencia(request.agencia());
        existente.setNumeroConta(request.numeroConta());
        existente.setTipoConta(request.tipoConta());
        existente.setSaldoAtual(request.saldoAtual());

        ContaFinanceira atualizada = contaFinanceiraRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    public Optional<ContaFinanceiraResponse> buscarPorId(Long id) {
        return contaFinanceiraRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<ContaFinanceiraResponse> listarTodas() {
        return contaFinanceiraRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        contaFinanceiraRepository.deleteById(id);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    public List<ContaFinanceiraResponse> listarPorTipo(TipoConta tipoConta) {
        return contaFinanceiraRepository.findByTipoConta(tipoConta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ContaFinanceiraResponse> buscarPorDescricao(String descricao) {
        return contaFinanceiraRepository.findByDescricaoContainingIgnoreCase(descricao)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 💼 NEGÓCIO
    // ==================================

    @Override
    public BigDecimal calcularSaldoTotal() {
        return contaFinanceiraRepository.calcularSaldoTotal() != null
                ? contaFinanceiraRepository.calcularSaldoTotal()
                : BigDecimal.ZERO;
    }

    @Override
    public Long contarContasNegativas() {
        return contaFinanceiraRepository.contarContasNegativas();
    }

    @Override
    public ContaFinanceiraResponse atualizarSaldo(Long id, BigDecimal valor) {
        ContaFinanceira conta = contaFinanceiraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta financeira não encontrada."));

        conta.setSaldoAtual(valor);
        return toResponse(contaFinanceiraRepository.save(conta));
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private ContaFinanceiraResponse toResponse(ContaFinanceira conta) {
        return mapper.map(conta, ContaFinanceiraResponse.class);
    }
}