package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.enums.Uf;
import br.com.unicos.ms_pessoas.mapper.MunicipioMapper;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.repository.MunicipioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MunicipioService {

    private final MunicipioRepository repository;
    private final MunicipioMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // Criar
    // ============================================================
    @Transactional
    public MunicipioResponse criar(MunicipioRequest request) {
        // Evita nome duplicado
        repository.findByNome(request.nome()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe um município com este nome.");
        });

        // Evita IBGE duplicado (se informado)
        if (request.codigoIbge() != null)
            repository.findByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                throw new IllegalArgumentException("Já existe um município com este código IBGE.");
            });

        Municipio municipio = mapper.toEntity(request);
        repository.save(municipio);

        return mapper.toResponse(municipio);
    }

    // ============================================================
    // Atualizar
    // ============================================================
    @Transactional
    public MunicipioResponse atualizar(Long id, MunicipioRequest request) {
        Municipio municipio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        // Validação de nome duplicado
        repository.findByNome(request.nome()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro município com este nome.");
        });

        // Validação de IBGE duplicado
        if (request.codigoIbge() != null)
            repository.findByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                if (!existing.getId().equals(id))
                    throw new IllegalArgumentException("Já existe outro município com este código IBGE.");
            });

        modelMapper.map(request, municipio);
        repository.save(municipio);

        return mapper.toResponse(municipio);
    }

    // ============================================================
    // Excluir
    // ============================================================
    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Município não encontrado.");
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<MunicipioResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Nome (contains ignore case)
    // ============================================================

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por UF
    // ============================================================

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarPorUf(String uf) {
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
    // Buscar por Código IBGE
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<MunicipioResponse> buscarPorCodigoIbge(String codigoIbge) {
        return repository.findByCodigoIbge(codigoIbge)
                .map(mapper::toResponse);
    }
}
