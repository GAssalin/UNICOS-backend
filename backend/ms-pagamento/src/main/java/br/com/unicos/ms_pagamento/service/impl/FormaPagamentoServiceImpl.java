package br.com.unicos.ms_pagamento.service.impl;

import br.com.unicos.ms_pagamento.dto.FormaPagamentoRequest;
import br.com.unicos.ms_pagamento.dto.FormaPagamentoResponse;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
import br.com.unicos.ms_pagamento.repository.FormaPagamentoRepository;
import br.com.unicos.ms_pagamento.service.FormaPagamentoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface FormaPagamentoService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de formas de pagamento.
 */
@Service
@Transactional
public class FormaPagamentoServiceImpl implements FormaPagamentoService {

    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ModelMapper mapper;

    public FormaPagamentoServiceImpl(FormaPagamentoRepository formaPagamentoRepository, ModelMapper mapper) {
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public FormaPagamentoResponse salvar(FormaPagamentoRequest request) {
        FormaPagamento forma = mapper.map(request, FormaPagamento.class);
        FormaPagamento salva = formaPagamentoRepository.save(forma);
        return toResponse(salva);
    }

    @Override
    public FormaPagamentoResponse atualizar(Long id, FormaPagamentoRequest request) {
        FormaPagamento existente = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forma de pagamento não encontrada com ID: " + id));

        existente.setDescricao(request.descricao());
        existente.setTipo(request.tipo());
        existente.setAtivo(request.ativo());

        FormaPagamento atualizada = formaPagamentoRepository.save(existente);
        return toResponse(atualizada);
    }

    @Override
    public Optional<FormaPagamentoResponse> buscarPorId(Long id) {
        return formaPagamentoRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<FormaPagamentoResponse> listarTodas() {
        return formaPagamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        formaPagamentoRepository.deleteById(id);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    public List<FormaPagamentoResponse> listarAtivas() {
        return formaPagamentoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<FormaPagamentoResponse> listarPorTipo(TipoFormaPagamento tipo) {
        return formaPagamentoRepository.findByTipo(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<FormaPagamentoResponse> buscarPorDescricao(String descricao) {
        return formaPagamentoRepository.findByDescricaoContainingIgnoreCase(descricao)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 💼 NEGÓCIO
    // ==================================

    @Override
    public FormaPagamentoResponse ativar(Long id) {
        FormaPagamento forma = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forma de pagamento não encontrada."));
        forma.setAtivo(true);
        return toResponse(formaPagamentoRepository.save(forma));
    }

    @Override
    public FormaPagamentoResponse inativar(Long id) {
        FormaPagamento forma = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forma de pagamento não encontrada."));
        forma.setAtivo(false);
        return toResponse(formaPagamentoRepository.save(forma));
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private FormaPagamentoResponse toResponse(FormaPagamento forma) {
        return mapper.map(forma, FormaPagamentoResponse.class);
    }
}
