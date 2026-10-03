package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.enums.Uf;
import br.com.unicos.ms_pessoas.mapper.MunicipioMapper;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.repository.MunicipioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Regras de negócio aplicadas aos municípios.
 *
 * <p>
 * Municípios são dados de referência compartilhados por todas as empresas: qualquer empresa
 * pode consultá-los e utilizá-los em endereços, mas apenas a empresa que cadastrou o registro
 * pode alterá-lo ou excluí-lo.
 * </p>
 */
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
    public MunicipioResponse criar(MunicipioRequest request) {
        validarDuplicidade(request, null);

        Municipio municipio = mapper.toEntity(request);
        municipio.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(municipio));
    }

    @Transactional
    public MunicipioResponse atualizar(Long id, MunicipioRequest request) {
        Municipio municipio = buscarMunicipioDaEmpresa(id);

        validarDuplicidade(request, id);
        mapper.updateEntity(municipio, request);

        return mapper.toResponse(repository.save(municipio));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarMunicipioDaEmpresa(id));
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<MunicipioListDTO> listarTodos() {
        return repository.findAllByOrderByNomeAsc()
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
        return repository.findByUf(converterUf(uf))
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioResponse> buscarPorCodigoIbge(String codigoIbge) {
        return repository.findFirstByCodigoIbge(codigoIbge)
                .map(mapper::toResponse);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Municipio buscarMunicipioDaEmpresa(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado."));
    }

    /**
     * Municípios homônimos existem em UFs diferentes; a duplicidade considera nome + UF e o código IBGE.
     */
    private void validarDuplicidade(MunicipioRequest request, Long idAtual) {
        repository.findFirstByNomeIgnoreCaseAndUf(request.nome(), request.uf()).ifPresent(existing -> {
            if (!existing.getId().equals(idAtual))
                throw new IllegalArgumentException("Já existe um município com este nome nesta UF.");
        });

        if (request.codigoIbge() != null && !request.codigoIbge().isBlank())
            repository.findFirstByCodigoIbge(request.codigoIbge()).ifPresent(existing -> {
                if (!existing.getId().equals(idAtual))
                    throw new IllegalArgumentException("Já existe um município com este código IBGE.");
            });
    }

    private static Uf converterUf(String uf) {
        try {
            return Uf.valueOf(uf.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("UF inválida: " + uf);
        }
    }
}
