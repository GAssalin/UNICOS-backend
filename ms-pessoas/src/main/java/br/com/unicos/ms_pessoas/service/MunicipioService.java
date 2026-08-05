package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.enums.Uf;
import br.com.unicos.ms_pessoas.mapper.MunicipioMapper;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.repository.MunicipioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService extends BaseTenantService<Municipio, Long> {

    private final MunicipioRepository repository;
    private final MunicipioMapper mapper;

    public MunicipioService(MunicipioRepository repository, MunicipioMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdmin")
    public MunicipioResponse criar(MunicipioRequest request) {
        repository.findByNome(request.nome()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe um município com este nome.");
        });

        if (request.codigoIbge() != null)
            repository.findByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                throw new IllegalArgumentException("Já existe um município com este código IBGE.");
            });

        Municipio municipio = mapper.toEntity(request);
        repository.save(municipio);

        return mapper.toResponse(municipio);
    }

    @Transactional
    @CircuitBreaker(name = "pessoa-municipio-admin", fallbackMethod = "fallbackAdmin")
    public MunicipioResponse atualizar(Long id, MunicipioRequest request) {
        Municipio municipio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        repository.findByNome(request.nome()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro município com este nome.");
        });

        if (request.codigoIbge() != null)
            repository.findByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                if (!existing.getId().equals(id))
                    throw new IllegalArgumentException("Já existe outro município com este código IBGE.");
            });

        mapper.toEntity(request);
        repository.save(municipio);

        return mapper.toResponse(municipio);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Município não encontrado.");

        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarPorUf(String uf) {
        return repository.findByUf(Uf.valueOf(uf.toUpperCase()))
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioResponse> buscarPorCodigoIbge(String codigoIbge) {
        return repository.findByCodigoIbge(codigoIbge)
                .map(mapper::toResponse);
    }

}
