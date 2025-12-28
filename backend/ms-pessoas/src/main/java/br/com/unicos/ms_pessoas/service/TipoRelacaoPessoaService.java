package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.mapper.TipoRelacaoPessoaMapper;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import br.com.unicos.ms_pessoas.repository.TipoRelacaoPessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio para o gerenciamento dos tipos de
 * relação entre pessoas dentro do UniCoS.
 */
@Service
@RequiredArgsConstructor
public class TipoRelacaoPessoaService {

    private final TipoRelacaoPessoaRepository repository;
    private final TipoRelacaoPessoaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================
    @Transactional
    public TipoRelacaoPessoaResponse criar(TipoRelacaoPessoaRequest request) {
        // Validação: nome deve ser único
        repository.findByNome(request.nome()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe um tipo de relação com este nome.");
        });

        TipoRelacaoPessoa entity = mapper.toEntity(request);
        repository.save(entity);

        return mapper.toResponse(entity);
    }

    // ============================================================
    // Atualizar
    // ============================================================
    @Transactional
    public TipoRelacaoPessoaResponse atualizar(Long id, TipoRelacaoPessoaRequest request) {
        TipoRelacaoPessoa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        // Validação de nome único para atualização
        repository.findByNome(request.nome()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro tipo de relação com este nome.");
        });

        modelMapper.map(request, entity);
        repository.save(entity);

        return mapper.toResponse(entity);
    }

    // ============================================================
    // Excluir
    // ============================================================
    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Tipo de relação não encontrado.");
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<TipoRelacaoPessoaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Transactional(readOnly = true)
    public List<TipoRelacaoPessoaListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Nome (contains)
    // ============================================================

    @Transactional(readOnly = true)
    public List<TipoRelacaoPessoaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Buscar por Nome Exato
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<TipoRelacaoPessoaResponse> buscarPorNomeExato(String nome) {
        return repository.findByNome(nome)
                .map(mapper::toResponse);
    }
}
