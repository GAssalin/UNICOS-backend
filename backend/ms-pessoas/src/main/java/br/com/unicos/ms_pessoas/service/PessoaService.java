package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.client.PermissaoClient;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.mapper.PessoaMapper;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das operações genéricas de consulta aplicadas à entidade {@link br.com.unicos.ms_pessoas.model.Pessoa},
 * que serve como base para Pessoa Física e Pessoa Jurídica.
 */
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository repository;
    private final PessoaMapper mapper;

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<PessoaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-admin", fallbackMethod = "fallbackAdminList")
    public List<PessoaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-admin", fallbackMethod = "fallbackAdminListNome")
    public List<PessoaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-admin", fallbackMethod = "fallbackAdminListNomeExato")
    public List<PessoaListDTO> listarPorNomeExato(String nome) {
        return repository.findByNome(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-admin", fallbackMethod = "fallbackAdminListTipo")
    public List<PessoaListDTO> listarPorTipo(String tipoPessoa) {
        TipoPessoa tipoEnum;
        try {
            tipoEnum = TipoPessoa.valueOf(tipoPessoa.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de pessoa inválido: " + tipoPessoa);
        }

        return repository.findByTipoPessoa(tipoEnum)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private Optional<PessoaResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoas temporariamente indisponível");
    }

    private List<PessoaListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoas temporariamente indisponível");
    }

    private List<PessoaListDTO> fallbackAdminListNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoas temporariamente indisponível");
    }

    private List<PessoaListDTO> fallbackAdminListNomeExato(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoas temporariamente indisponível");
    }

    private List<PessoaListDTO> fallbackAdminListTipo(String tipoPessoa, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoas temporariamente indisponível");
    }
}
