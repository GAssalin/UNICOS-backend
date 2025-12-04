package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.mapper.UnidadeMedidaMapper;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import br.com.unicos.ms_produtos.repository.UnidadeMedidaRepository;
import br.com.unicos.ms_produtos.service.UnidadeMedidaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link UnidadeMedidaService}
 * utilizando ModelMapper para conversão entre entidades e DTOs.
 */
@Service
@RequiredArgsConstructor
public class UnidadeMedidaServiceImpl implements UnidadeMedidaService {

    private final UnidadeMedidaRepository repository;
    private final UnidadeMedidaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================

    @Override
    @Transactional
    public UnidadeMedidaResponse salvar(UnidadeMedidaRequest request) {

        if (repository.existsBySiglaIgnoreCase(request.sigla())) {
            throw new IllegalArgumentException("Já existe uma unidade com esta sigla.");
        }

        UnidadeMedida unidade = new UnidadeMedida();
        unidade.setNome(request.nome());
        unidade.setSigla(request.sigla());
        unidade.setDescricao(request.descricao());
        unidade.setAtivo(true);

        UnidadeMedida salvo = repository.save(unidade);

        return mapper.toResponse(salvo);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @Override
    @Transactional
    public UnidadeMedidaResponse atualizar(Long id, UnidadeMedidaRequest request) {

        UnidadeMedida entidade = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada."));

        // Valida duplicidade de sigla
        Optional<UnidadeMedida> outra = repository.findBySiglaIgnoreCase(request.sigla());
        if (outra.isPresent() && !outra.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe outra unidade com esta sigla.");
        }

        // ModelMapper atualiza os campos automaticamente
        modelMapper.map(request, entidade);

        UnidadeMedida atualizado = repository.save(entidade);
        return mapper.toResponse(atualizado);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedidaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar todas (detalhado)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedidaResponse> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // Excluir
    // ============================================================

    @Override
    @Transactional
    public void deletar(Long id) {
        var unidade = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada."));
        repository.delete(unidade);
    }

    // ============================================================
    // Buscar por nome
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedidaResponse> buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Buscar por sigla
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedidaResponse> buscarPorSigla(String sigla) {
        return repository.findBySiglaIgnoreCase(sigla)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Buscar por nome contendo
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedidaListDTO> buscarPorNomeContendo(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listagem simples ordenada
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedidaListDTO> listarSimples() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Verificar sigla
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public boolean verificarSiglaExistente(String sigla) {
        return repository.existsBySiglaIgnoreCase(sigla);
    }
}
