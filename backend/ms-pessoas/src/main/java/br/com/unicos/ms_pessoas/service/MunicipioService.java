package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.client.AuthClient;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.enums.Uf;
import br.com.unicos.ms_pessoas.mapper.MunicipioMapper;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.repository.MunicipioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MunicipioService {

    private final AuthClient authClient;
    private final MunicipioRepository repository;
    private final MunicipioMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdmin")
    public MunicipioResponse criar(MunicipioRequest request) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar municípios.");

        repository.findByNome(request.nome()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe um município com este nome.");
        });

        if (request.codigoIbge() != null) {
            repository.findByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                throw new IllegalArgumentException("Já existe um município com este código IBGE.");
            });
        }

        Municipio municipio = mapper.toEntity(request);
        repository.save(municipio);

        return mapper.toResponse(municipio);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdmin")
    public MunicipioResponse atualizar(Long id, MunicipioRequest request) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar municípios.");

        Municipio municipio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        repository.findByNome(request.nome()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro município com este nome.");
        });

        if (request.codigoIbge() != null) {
            repository.findByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                if (!existing.getId().equals(id))
                    throw new IllegalArgumentException("Já existe outro município com este código IBGE.");
            });
        }

        modelMapper.map(request, municipio);
        repository.save(municipio);

        return mapper.toResponse(municipio);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir municípios.");

        if (!repository.existsById(id))
            throw new EntityNotFoundException("Município não encontrado.");

        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<MunicipioResponse> buscarPorId(Long id) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar municípios.");
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdminList")
    public List<MunicipioListDTO> listarTodos() {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar municípios.");
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdminListNome")
    public List<MunicipioListDTO> listarPorNome(String nome) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar municípios.");
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdminListUf")
    public List<MunicipioListDTO> listarPorUf(String uf) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar municípios.");

        Uf ufEnum;
        try {
            ufEnum = Uf.valueOf(uf.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UF inválida: " + uf);
        }

        return repository.findByUf(ufEnum)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // IBGE
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdminOptionalIbge")
    public Optional<MunicipioResponse> buscarPorCodigoIbge(String codigoIbge) {
        if (!authClient.usuarioPossuiPermissao("PESSOA_MUNICIPIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar municípios.");
        return repository.findByCodigoIbge(codigoIbge)
                .map(mapper::toResponse);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private MunicipioResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }

    private Optional<MunicipioResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }

    private Optional<MunicipioResponse> fallbackAdminOptionalIbge(String codigoIbge, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }

    private List<MunicipioListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }

    private List<MunicipioListDTO> fallbackAdminListNome(String nome, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }

    private List<MunicipioListDTO> fallbackAdminListUf(String uf, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de municípios temporariamente indisponível");
    }
}
