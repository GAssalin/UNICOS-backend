package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.client.PermissaoClient;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaFisicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import br.com.unicos.ms_pessoas.repository.PessoaFisicaRepository;
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
 * Implementação das regras de negócio para cadastro e consulta
 * de Pessoas Físicas no UniCoS.
 */
@Service
@RequiredArgsConstructor
public class PessoaFisicaService {

    private final PermissaoClient permissaoClient;
    private final PessoaFisicaRepository repository;
    private final PessoaFisicaMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdmin")
    public PessoaFisicaResponse criar(PessoaFisicaRequest request) {
        repository.findByCpf(request.cpf()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe uma pessoa física cadastrada com este CPF.");
        });

        PessoaFisica pessoa = mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdmin")
    public PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request) {
        PessoaFisica pessoa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada."));

        repository.findByCpf(request.cpf()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outra pessoa física com este CPF.");
        });

        modelMapper.map(request, pessoa);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Pessoa Física não encontrada.");

        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<PessoaFisicaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdminOptionalCpf")
    public Optional<PessoaFisicaResponse> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdminList")
    public List<PessoaFisicaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdminListNomeSocial")
    public List<PessoaFisicaListDTO> listarPorNomeSocial(String nomeSocial) {
        return repository.findByNomeSocial(nomeSocial)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-fisica-admin", fallbackMethod = "fallbackAdminListNome")
    public List<PessoaFisicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private PessoaFisicaResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }

    private Optional<PessoaFisicaResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }

    private Optional<PessoaFisicaResponse> fallbackAdminOptionalCpf(String cpf, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }

    private List<PessoaFisicaListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }

    private List<PessoaFisicaListDTO> fallbackAdminListNomeSocial(String nomeSocial, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }

    private List<PessoaFisicaListDTO> fallbackAdminListNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoa física temporariamente indisponível");
    }
}
