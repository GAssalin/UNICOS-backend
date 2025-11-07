package br.com.unicos.ms_compras.service.requisicao.impl;

import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraListDTO;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraRequest;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraResponse;
import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;
import br.com.unicos.ms_compras.model.requisicao.RequisicaoCompra;
import br.com.unicos.ms_compras.repository.requisicao.RequisicaoCompraRepository;
import br.com.unicos.ms_compras.service.requisicao.RequisicaoCompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link RequisicaoCompraService}.
 *
 * <p>
 * Responsável pelo gerenciamento das requisições internas de compra,
 * controlando datas, tipos, solicitantes e observações.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RequisicaoCompraServiceImpl implements RequisicaoCompraService {

    private final RequisicaoCompraRepository requisicaoCompraRepository;
    private final ModelMapper modelMapper;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    @Override
    @Transactional
    public RequisicaoCompraResponse criar(RequisicaoCompraRequest request) {
        RequisicaoCompra requisicao = modelMapper.map(request, RequisicaoCompra.class);
        requisicao.setDataAbertura(LocalDate.now());

        RequisicaoCompra salvo = requisicaoCompraRepository.save(requisicao);
        return modelMapper.map(salvo, RequisicaoCompraResponse.class);
    }

    @Override
    @Transactional
    public RequisicaoCompraResponse atualizar(Long id, RequisicaoCompraRequest request) {
        RequisicaoCompra existente = requisicaoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Requisição de compra não encontrada para o ID: " + id));

        modelMapper.map(request, existente);
        RequisicaoCompra atualizado = requisicaoCompraRepository.save(existente);
        return modelMapper.map(atualizado, RequisicaoCompraResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RequisicaoCompraResponse> buscarPorId(Long id) {
        return requisicaoCompraRepository.findById(id)
                .map(r -> modelMapper.map(r, RequisicaoCompraResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequisicaoCompraListDTO> listar(Pageable pageable) {
        return requisicaoCompraRepository.findAll(pageable)
                .map(r -> modelMapper.map(r, RequisicaoCompraListDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisicaoCompraListDTO> listarPorTipo(TipoRequisicaoCompra tipo) {
        return requisicaoCompraRepository.findByTipoRequisicao(tipo).stream()
                .map(r -> modelMapper.map(r, RequisicaoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisicaoCompraListDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return requisicaoCompraRepository.findByDataAberturaBetween(inicio, fim).stream()
                .map(r -> modelMapper.map(r, RequisicaoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisicaoCompraListDTO> listarPorSolicitante(Long solicitanteId) {
        return requisicaoCompraRepository.findBySolicitanteId(solicitanteId).stream()
                .map(r -> modelMapper.map(r, RequisicaoCompraListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!requisicaoCompraRepository.existsById(id)) {
            throw new EntityNotFoundException("Requisição de compra não encontrada para exclusão. ID: " + id);
        }
        requisicaoCompraRepository.deleteById(id);
    }
}
