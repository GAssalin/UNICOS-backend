package br.com.unicos.ms_ativos.service.impl;

import br.com.unicos.ms_ativos.dto.AtivoRequest;
import br.com.unicos.ms_ativos.dto.AtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import br.com.unicos.ms_ativos.model.Ativo;
import br.com.unicos.ms_ativos.repository.AtivoRepository;
import br.com.unicos.ms_ativos.service.AtivoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link AtivoService}.
 * <p>
 * Contém as regras de negócio e interações com o repositório de Ativo.
 */
@Service
@Transactional
public class AtivoServiceImpl implements AtivoService {

    private final AtivoRepository ativoRepository;

    public AtivoServiceImpl(AtivoRepository ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public AtivoResponse salvar(AtivoRequest request) {
        if (ativoRepository.findByCodigoPatrimonial(request.codigoPatrimonial()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um ativo cadastrado com este código patrimonial.");
        }

        Ativo entity = toEntity(request);
        Ativo salvo = ativoRepository.save(entity);
        return toResponse(salvo);
    }

    @Override
    public AtivoResponse atualizar(Long id, AtivoRequest request) {
        Ativo existente = ativoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ativo não encontrado."));

        // Verifica duplicidade de código patrimonial
        ativoRepository.findByCodigoPatrimonial(request.codigoPatrimonial())
                .ifPresent(outro -> {
                    if (!outro.getId().equals(id)) {
                        throw new DataIntegrityViolationException("Já existe um ativo cadastrado com este código patrimonial.");
                    }
                });

        // Atualiza manualmente os campos
        existente.setNome(request.nome());
        existente.setCodigoPatrimonial(request.codigoPatrimonial());
        existente.setDescricao(request.descricao());
        existente.setTipo(request.tipo());
        existente.setStatus(request.status());
        existente.setDataAquisicao(request.dataAquisicao());
        existente.setValorAquisicao(request.valorAquisicao());
        existente.setValorAtual(request.valorAtual());
        existente.setEmpresaId(request.empresaId());
        existente.setFilialId(request.filialId());
        existente.setResponsavelId(request.responsavelId());
        existente.getLocalizacao().setId(request.localizacaoId());

        Ativo atualizado = ativoRepository.save(existente);
        return toResponse(atualizado);
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    @Override
    @Transactional(readOnly = true)
    public Optional<AtivoResponse> buscarPorId(Long id) {
        return ativoRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AtivoResponse> buscarPorCodigoPatrimonial(String codigoPatrimonial) {
        return ativoRepository.findByCodigoPatrimonial(codigoPatrimonial)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtivoResponse> listarTodos() {
        return ativoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtivoResponse> buscarPorEmpresa(Long empresaId) {
        return ativoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtivoResponse> buscarPorFilial(Long filialId) {
        return ativoRepository.findByFilialId(filialId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtivoResponse> buscarPorTipo(TipoAtivo tipo) {
        return ativoRepository.findByTipo(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtivoResponse> buscarPorStatus(StatusAtivo status) {
        return ativoRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================================
    // 🔹 DELETE
    // ==================================

    @Override
    public void deletar(Long id) {
        if (!ativoRepository.existsById(id)) {
            throw new EntityNotFoundException("Ativo não encontrado para exclusão.");
        }
        ativoRepository.deleteById(id);
    }

    // ==================================
    // 🧭 MÉTODOS AUXILIARES
    // ==================================

    private Ativo toEntity(AtivoRequest request) {
        Ativo ativo = new Ativo();
        ativo.setNome(request.nome());
        ativo.setCodigoPatrimonial(request.codigoPatrimonial());
        ativo.setDescricao(request.descricao());
        ativo.setTipo(request.tipo());
        ativo.setStatus(request.status());
        ativo.setDataAquisicao(request.dataAquisicao());
        ativo.setValorAquisicao(request.valorAquisicao());
        ativo.setValorAtual(request.valorAtual());
        ativo.setEmpresaId(request.empresaId());
        ativo.setFilialId(request.filialId());
        ativo.setResponsavelId(request.responsavelId());
        ativo.getLocalizacao().setId(request.localizacaoId());
        return ativo;
    }

    private AtivoResponse toResponse(Ativo entity) {
        return new AtivoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCodigoPatrimonial(),
                entity.getDescricao(),
                entity.getTipo(),
                entity.getStatus(),
                entity.getDataAquisicao(),
                entity.getValorAquisicao(),
                entity.getValorAtual(),
                entity.getEmpresaId(),
                entity.getFilialId(),
                entity.getResponsavelId(),
                entity.getLocalizacao().getId()
        );
    }
}