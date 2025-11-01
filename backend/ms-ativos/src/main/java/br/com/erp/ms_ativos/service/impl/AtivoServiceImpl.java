package br.com.erp.ms_ativos.service.impl;

import br.com.erp.ms_ativos.dto.AtivoRequest;
import br.com.erp.ms_ativos.dto.AtivoResponse;
import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import br.com.erp.ms_ativos.model.Ativo;
import br.com.erp.ms_ativos.repository.AtivoRepository;
import br.com.erp.ms_ativos.service.AtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link AtivoService}.
 *
 * Contém as regras de negócio e interações com o repositório de Ativo.
 */
@Service
@RequiredArgsConstructor
public class AtivoServiceImpl implements AtivoService {

    private final AtivoRepository ativoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AtivoResponse salvar(AtivoRequest request) {
        if (ativoRepository.findByCodigoPatrimonial(request.getCodigoPatrimonial()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um ativo cadastrado com este código patrimonial.");
        }

        Ativo entity = modelMapper.map(request, Ativo.class);
        Ativo salvo = ativoRepository.save(entity);
        return modelMapper.map(salvo, AtivoResponse.class);
    }

    @Override
    @Transactional
    public AtivoResponse atualizar(Long id, AtivoRequest request) {
        Ativo existente = ativoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ativo não encontrado."));

        if (!existente.getCodigoPatrimonial().equals(request.getCodigoPatrimonial())
                && ativoRepository.findByCodigoPatrimonial(request.getCodigoPatrimonial()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um ativo cadastrado com este código patrimonial.");
        }

        modelMapper.map(request, existente);
        Ativo atualizado = ativoRepository.save(existente);
        return modelMapper.map(atualizado, AtivoResponse.class);
    }

    @Override
    public Optional<AtivoResponse> buscarPorId(Long id) {
        return ativoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, AtivoResponse.class));
    }

    @Override
    public Optional<AtivoResponse> buscarPorCodigoPatrimonial(String codigoPatrimonial) {
        return ativoRepository.findByCodigoPatrimonial(codigoPatrimonial)
                .map(entity -> modelMapper.map(entity, AtivoResponse.class));
    }

    @Override
    public List<AtivoResponse> listarTodos() {
        return ativoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoResponse.class))
                .toList();
    }

    @Override
    public List<AtivoResponse> buscarPorEmpresa(Long empresaId) {
        return ativoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoResponse.class))
                .toList();
    }

    @Override
    public List<AtivoResponse> buscarPorFilial(Long filialId) {
        return ativoRepository.findByFilialId(filialId)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoResponse.class))
                .toList();
    }

    @Override
    public List<AtivoResponse> buscarPorTipo(TipoAtivo tipo) {
        return ativoRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoResponse.class))
                .toList();
    }

    @Override
    public List<AtivoResponse> buscarPorStatus(StatusAtivo status) {
        return ativoRepository.findByStatus(status)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!ativoRepository.existsById(id)) {
            throw new EntityNotFoundException("Ativo não encontrado para exclusão.");
        }
        ativoRepository.deleteById(id);
    }
}