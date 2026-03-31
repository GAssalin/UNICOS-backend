package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaJuridicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import br.com.unicos.ms_pessoas.repository.PessoaJuridicaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio para cadastro e consulta
 * de Pessoas Jurídicas no UniCoS.
 */
@Service
@RequiredArgsConstructor
public class PessoaJuridicaService {

    private final PessoaJuridicaRepository repository;
    private final PessoaJuridicaMapper mapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdmin")
    public PessoaJuridicaResponse criar(PessoaJuridicaRequest request) {
        repository.findByCnpj(request.cnpj()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe uma pessoa jurídica cadastrada com este CNPJ.");
        });

        PessoaJuridica pessoa = mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdmin")
    public PessoaJuridicaResponse atualizar(Long id, PessoaJuridicaRequest request) {
        PessoaJuridica pessoa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Jurídica não encontrada."));

        repository.findByCnpj(request.cnpj()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outra pessoa jurídica com este CNPJ.");
        });

        mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Pessoa Jurídica não encontrada.");
        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<PessoaJuridicaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdminOptionalCnpj")
    public Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdminList")
    public List<PessoaJuridicaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdminListNomeFantasia")
    public List<PessoaJuridicaListDTO> listarPorNomeFantasia(String nomeFantasia) {
        return repository.findByNomeFantasia(nomeFantasia)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-juridica-admin", fallbackMethod = "fallbackAdminListNome")
    public List<PessoaJuridicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private PessoaJuridicaResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }

    private Optional<PessoaJuridicaResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }

    private Optional<PessoaJuridicaResponse> fallbackAdminOptionalCnpj(String cnpj, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }

    private List<PessoaJuridicaListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }

    private List<PessoaJuridicaListDTO> fallbackAdminListNomeFantasia(String nomeFantasia, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }

    private List<PessoaJuridicaListDTO> fallbackAdminListNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa jurídica temporariamente indisponível");
    }
}
