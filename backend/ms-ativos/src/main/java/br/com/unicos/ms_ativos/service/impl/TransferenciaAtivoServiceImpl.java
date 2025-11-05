package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.TransferenciaAtivoListDTO;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoRequest;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoResponse;
import br.com.unicos.ms_ativos.enums.TipoTransferencia;
import br.com.unicos.ms_ativos.model.TransferenciaAtivo;
import br.com.unicos.ms_ativos.repository.TransferenciaAtivoRepository;
import br.com.unicos.ms_ativos.service.TransferenciaAtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link TransferenciaAtivoService}.
 * <p>
 * Responsável pela gestão e rastreabilidade das movimentações de ativos,
 * incluindo registro, atualização, exclusão e geração de relatórios.
 */
@Service
@RequiredArgsConstructor
public class TransferenciaAtivoServiceImpl implements TransferenciaAtivoService {

    private final TransferenciaAtivoRepository transferenciaRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    @Override
    @Transactional
    public TransferenciaAtivoResponse salvar(TransferenciaAtivoRequest request) {
        // Valida duplicidade: transferência no mesmo dia e entre as mesmas unidades
        boolean duplicada = transferenciaRepository.findByAtivoId(request.ativoId())
                .stream()
                .anyMatch(t ->
                        t.getDataTransferencia().equals(request.dataTransferencia()) &&
                                t.getOrigemId().equals(request.origemId()) &&
                                t.getDestinoId().equals(request.destinoId())
                );

        if (duplicada) {
            throw new DataIntegrityViolationException("Já existe uma transferência registrada entre as mesmas unidades nesta data.");
        }

        TransferenciaAtivo transferencia = modelMapper.map(request, TransferenciaAtivo.class);
        TransferenciaAtivo salva = transferenciaRepository.save(transferencia);
        return modelMapper.map(salva, TransferenciaAtivoResponse.class);
    }

    @Override
    @Transactional
    public TransferenciaAtivoResponse atualizar(Long id, TransferenciaAtivoRequest request) {
        TransferenciaAtivo existente = transferenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transferência não encontrada com ID: " + id));

        existente.setOrigemId(request.origemId());
        existente.setDestinoId(request.destinoId());
        existente.setTipo(request.tipo());
        existente.setDataTransferencia(request.dataTransferencia());
        existente.setResponsavelId(request.responsavelId());
        existente.setMotivo(request.motivo());

        TransferenciaAtivo atualizada = transferenciaRepository.save(existente);
        return modelMapper.map(atualizada, TransferenciaAtivoResponse.class);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        TransferenciaAtivo transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transferência não encontrada com ID: " + id));

        // Bloqueia exclusão de transferências antigas para manter rastreabilidade
        if (transferencia.getDataTransferencia().isBefore(LocalDate.now().minusDays(30))) {
            throw new DataIntegrityViolationException("Não é permitido excluir transferências com mais de 30 dias.");
        }

        transferenciaRepository.delete(transferencia);
    }

    @Override
    public Optional<TransferenciaAtivoResponse> buscarPorId(Long id) {
        return transferenciaRepository.findById(id)
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class));
    }

    @Override
    public List<TransferenciaAtivoListDTO> listarTodos() {
        return transferenciaRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    @Override
    public List<TransferenciaAtivoListDTO> buscarPorAtivo(Long ativoId) {
        return transferenciaRepository.findByAtivoId(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferenciaAtivoListDTO> buscarPorOrigem(Long origemId) {
        return transferenciaRepository.findByOrigemId(origemId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferenciaAtivoListDTO> buscarPorDestino(Long destinoId) {
        return transferenciaRepository.findByDestinoId(destinoId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferenciaAtivoListDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return transferenciaRepository.findByDataTransferenciaBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferenciaAtivoListDTO> buscarPorResponsavel(Long responsavelId) {
        return transferenciaRepository.buscarTransferenciasPorResponsavel(responsavelId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferenciaAtivoListDTO> buscarPorTipo(TipoTransferencia tipo) {
        return transferenciaRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 📊 RELATÓRIOS E RASTREABILIDADE
    // ===========================================================

    @Override
    public List<TransferenciaAtivoListDTO> buscarHistoricoDeMovimentacoes(Long ativoId) {
        return transferenciaRepository.findByAtivoId(ativoId)
                .stream()
                .sorted((a, b) -> b.getDataTransferencia().compareTo(a.getDataTransferencia()))
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long contarTotal() {
        return transferenciaRepository.count();
    }

    @Override
    public List<Object[]> contarPorTipo() {
        return transferenciaRepository.contarTransferenciasPorTipo();
    }

    @Override
    public List<Object[]> contarPorOrigem() {
        return transferenciaRepository.contarTransferenciasPorOrigem();
    }

    @Override
    public List<Object[]> contarPorDestino() {
        return transferenciaRepository.contarTransferenciasPorDestino();
    }

    @Override
    public List<TransferenciaAtivoListDTO> buscarRecentes(int limite) {
        return transferenciaRepository.buscarTransferenciasRecentes()
                .stream()
                .limit(limite)
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoListDTO.class))
                .collect(Collectors.toList());
    }
}
