package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.mapper.EnderecoMapper;
import br.com.unicos.ms_pessoas.model.Endereco;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.EnderecoRepository;
import br.com.unicos.ms_pessoas.repository.MunicipioRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Regras de negócio aplicadas aos endereços das pessoas.
 */
@Service
public class EnderecoService extends BaseTenantService<Endereco, Long> {

    private final EnderecoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final MunicipioRepository municipioRepository;
    private final EnderecoMapper mapper;

    public EnderecoService(EnderecoRepository repository, PessoaRepository pessoaRepository, MunicipioRepository municipioRepository, EnderecoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.municipioRepository = municipioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public EnderecoResponse criar(EnderecoRequest request) {
        Pessoa pessoa = buscarPessoa(request.pessoaId());
        Municipio municipio = buscarMunicipio(request.municipioId());

        if (Boolean.TRUE.equals(request.principal()))
            removerPrincipalExistente(pessoa, null);

        Endereco endereco = mapper.toEntity(request, pessoa, municipio);
        endereco.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(endereco));
    }

    @Transactional
    public EnderecoResponse atualizar(Long id, EnderecoRequest request) {
        Endereco endereco = buscarEndereco(id);
        Pessoa pessoa = buscarPessoa(request.pessoaId());
        Municipio municipio = buscarMunicipio(request.municipioId());

        if (Boolean.TRUE.equals(request.principal()))
            removerPrincipalExistente(pessoa, id);

        mapper.updateEntity(endereco, request, pessoa, municipio);

        return mapper.toResponse(repository.save(endereco));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEndereco(id));
    }

    @Transactional(readOnly = true)
    public Optional<EnderecoResponse> buscarPorId(Long id) {
        return findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarTodos() {
        return repository.findByEmpresaId(TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorPessoa(Long pessoaId) {
        return repository.findByPessoaAndEmpresaId(buscarPessoa(pessoaId), TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorPessoaETipo(Long pessoaId, String tipo) {
        return repository.findByPessoaAndTipoAndEmpresaId(buscarPessoa(pessoaId), converterTipo(tipo), TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorMunicipio(Long municipioId) {
        return repository.findByMunicipioAndEmpresaId(buscarMunicipio(municipioId), TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorCep(String cep) {
        return repository.findByCepAndEmpresaId(cep, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<EnderecoResponse> buscarPrincipal(Long pessoaId) {
        return repository.findByPessoaAndPrincipalTrueAndEmpresaId(buscarPessoa(pessoaId), TenantContext.getEmpresaId())
                .map(mapper::toResponse);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Endereco buscarEndereco(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado."));
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findByIdAndEmpresaId(pessoaId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
    }

    /**
     * Municípios são dados de referência compartilhados entre empresas.
     */
    private Municipio buscarMunicipio(Long municipioId) {
        return municipioRepository.findById(municipioId)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado."));
    }

    private void removerPrincipalExistente(Pessoa pessoa, Long idAtual) {
        repository.findByPessoaAndPrincipalTrueAndEmpresaId(pessoa, TenantContext.getEmpresaId())
                .filter(existing -> !existing.getId().equals(idAtual))
                .ifPresent(existing -> {
                    existing.setPrincipal(false);
                    repository.save(existing);
                });
    }

    private static TipoEndereco converterTipo(String tipo) {
        try {
            return TipoEndereco.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("Tipo de endereço inválido: " + tipo);
        }
    }
}
