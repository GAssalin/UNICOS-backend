package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.AtivoListDTO;
import br.com.unicos.ms_ativos.dto.AtivoRequest;
import br.com.unicos.ms_ativos.dto.AtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import br.com.unicos.ms_ativos.model.Ativo;
import br.com.unicos.ms_ativos.repository.AtivoRepository;
import br.com.unicos.ms_ativos.service.AtivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link AtivoService}.
 * <p>
 * Contém as regras de negócio relacionadas à gestão dos ativos patrimoniais.
 * Utiliza o {@link ModelMapper} para conversão entre entidades e DTOs.
 */
@Service
@RequiredArgsConstructor
public class AtivoServiceImpl implements AtivoService {

    private final AtivoRepository ativoRepository;
    private final ModelMapper modelMapper;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AtivoResponse salvar(AtivoRequest request) {
        Ativo ativo = modelMapper.map(request, Ativo.class);

        // Impede duplicidade de código patrimonial
        if (ativoRepository.findByCodigoPatrimonial(request.codigoPatrimonial()).isPresent()) {
            throw new DataIntegrityViolationException(
                    "Já existe um ativo com o código patrimonial informado: " + request.codigoPatrimonial()
            );
        }

        Ativo salvo = ativoRepository.save(ativo);
        return modelMapper.map(salvo, AtivoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AtivoResponse atualizar(Long id, AtivoRequest request) {
        Ativo existente = ativoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ativo não encontrado com ID: " + id));

        // Atualiza campos permitidos
        existente.setNome(request.nome());
        existente.setDescricao(request.descricao());
        existente.setTipo(request.tipo());
        existente.setStatus(request.status());
        existente.setDataAquisicao(request.dataAquisicao());
        existente.setValorAquisicao(request.valorAquisicao());
        existente.setValorAtual(request.valorAtual());
        existente.setEmpresaId(request.empresaId());
        existente.setFilialId(request.filialId());
        existente.setResponsavelId(request.responsavelId());

        Ativo atualizado = ativoRepository.save(existente);
        return modelMapper.map(atualizado, AtivoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        Ativo ativo = ativoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ativo não encontrado com ID: " + id));
        ativoRepository.delete(ativo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AtivoResponse> buscarPorId(Long id) {
        return ativoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, AtivoResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AtivoListDTO> listarTodos() {
        return ativoRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoListDTO.class))
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AtivoListDTO> buscarPorTipo(TipoAtivo tipo) {
        return ativoRepository.findByTipo(tipo)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AtivoListDTO> buscarPorStatus(StatusAtivo status) {
        return ativoRepository.findByStatus(status)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AtivoListDTO> buscarPorEmpresa(Long empresaId) {
        return ativoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AtivoListDTO> buscarPorFilial(Long filialId) {
        return ativoRepository.findByFilialId(filialId)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoListDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AtivoListDTO> buscarPorResponsavel(Long responsavelId) {
        return ativoRepository.findByResponsavelId(responsavelId)
                .stream()
                .map(entity -> modelMapper.map(entity, AtivoListDTO.class))
                .collect(Collectors.toList());
    }
}
