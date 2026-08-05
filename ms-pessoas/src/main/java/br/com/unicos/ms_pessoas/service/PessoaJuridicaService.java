package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaJuridicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import br.com.unicos.ms_pessoas.repository.PessoaJuridicaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio para cadastro e consulta
 * de Pessoas Jurídicas no UniCoS.
 */
@Service
public class PessoaJuridicaService extends BaseTenantService<PessoaJuridica, Long> {

    private final PessoaJuridicaRepository repository;
    private final PessoaJuridicaMapper mapper;

    public PessoaJuridicaService(PessoaJuridicaRepository repository, PessoaJuridicaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

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

    @Transactional
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

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Pessoa Jurídica não encontrada.");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarPorNomeFantasia(String nomeFantasia) {
        return repository.findByNomeFantasia(nomeFantasia)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaJuridicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

}
