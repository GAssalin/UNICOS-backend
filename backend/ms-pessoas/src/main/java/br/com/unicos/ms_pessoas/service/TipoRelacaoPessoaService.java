package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.mapper.TipoRelacaoPessoaMapper;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import br.com.unicos.ms_pessoas.repository.TipoRelacaoPessoaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdmin")
    public TipoRelacaoPessoaResponse criar(TipoRelacaoPessoaRequest request) {
        repository.findByNome(request.nome()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe um tipo de relação com este nome.");
        });

        TipoRelacaoPessoa entity = mapper.toEntity(request);
        repository.save(entity);

        return mapper.toResponse(entity);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdmin")
    public TipoRelacaoPessoaResponse atualizar(Long id, TipoRelacaoPessoaRequest request) {
        TipoRelacaoPessoa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de relação não encontrado."));

        repository.findByNome(request.nome()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro tipo de relação com este nome.");
        });

        modelMapper.map(request, entity);
        repository.save(entity);

        return mapper.toResponse(entity);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Tipo de relação não encontrado.");
        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<TipoRelacaoPessoaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdminList")
    public List<TipoRelacaoPessoaListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdminListNome")
    public List<TipoRelacaoPessoaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-tipo-relacao-admin", fallbackMethod = "fallbackAdminOptionalNome")
    public Optional<TipoRelacaoPessoaResponse> buscarPorNomeExato(String nome) {
        return repository.findByNome(nome)
                .map(mapper::toResponse);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private TipoRelacaoPessoaResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de relação entre pessoas temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de relação entre pessoas temporariamente indisponível");
    }

    private Optional<TipoRelacaoPessoaResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de relação entre pessoas temporariamente indisponível");
    }

    private Optional<TipoRelacaoPessoaResponse> fallbackAdminOptionalNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de relação entre pessoas temporariamente indisponível");
    }

    private List<TipoRelacaoPessoaListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de relação entre pessoas temporariamente indisponível");
    }

    private List<TipoRelacaoPessoaListDTO> fallbackAdminListNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de relação entre pessoas temporariamente indisponível");
    }
}
