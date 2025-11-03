package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.TransferenciaAtivoRequest;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoResponse;
import br.com.unicos.ms_ativos.model.TransferenciaAtivo;
import br.com.unicos.ms_ativos.repository.TransferenciaAtivoRepository;
import br.com.unicos.ms_ativos.service.TransferenciaAtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link TransferenciaAtivoService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de TransferenciaAtivo.
 */
@Service
@RequiredArgsConstructor
public class TransferenciaAtivoServiceImpl implements TransferenciaAtivoService {

    private final TransferenciaAtivoRepository transferenciaAtivoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransferenciaAtivoResponse salvar(TransferenciaAtivoRequest request) {
        TransferenciaAtivo entity = modelMapper.map(request, TransferenciaAtivo.class);
        TransferenciaAtivo salvo = transferenciaAtivoRepository.save(entity);
        return modelMapper.map(salvo, TransferenciaAtivoResponse.class);
    }

    @Override
    @Transactional
    public TransferenciaAtivoResponse atualizar(Long id, TransferenciaAtivoRequest request) {
        TransferenciaAtivo existente = transferenciaAtivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transferência não encontrada."));

        modelMapper.map(request, existente);
        TransferenciaAtivo atualizado = transferenciaAtivoRepository.save(existente);
        return modelMapper.map(atualizado, TransferenciaAtivoResponse.class);
    }

    @Override
    public Optional<TransferenciaAtivoResponse> buscarPorId(Long id) {
        return transferenciaAtivoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class));
    }

    @Override
    public List<TransferenciaAtivoResponse> listarTodas() {
        return transferenciaAtivoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class))
                .toList();
    }

    @Override
    public List<TransferenciaAtivoResponse> buscarPorAtivo(Long ativoId) {
        return transferenciaAtivoRepository.findByAtivoId(ativoId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class))
                .toList();
    }

    @Override
    public List<TransferenciaAtivoResponse> buscarPorOrigem(Long origemId) {
        return transferenciaAtivoRepository.findByOrigemId(origemId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class))
                .toList();
    }

    @Override
    public List<TransferenciaAtivoResponse> buscarPorDestino(Long destinoId) {
        return transferenciaAtivoRepository.findByDestinoId(destinoId)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class))
                .toList();
    }

    @Override
    public List<TransferenciaAtivoResponse> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return transferenciaAtivoRepository.findByDataTransferenciaBetween(inicio, fim)
                .stream()
                .map(entity -> modelMapper.map(entity, TransferenciaAtivoResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!transferenciaAtivoRepository.existsById(id)) {
            throw new EntityNotFoundException("Transferência não encontrada para exclusão.");
        }
        transferenciaAtivoRepository.deleteById(id);
    }
}