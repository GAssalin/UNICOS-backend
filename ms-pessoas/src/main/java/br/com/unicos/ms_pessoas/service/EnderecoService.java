package br.com.unicos.ms_pessoas.service;

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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação responsável pelas regras de negócio de endereços,
 * incluindo definição de endereço principal, validações e filtros
 * por município, tipo, CEP e pessoa.
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
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        Municipio municipio = municipioRepository.findById(request.municipioId())
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        Endereco endereco = mapper.toEntity(request, pessoa, municipio);
        endereco.setPessoa(pessoa);
        endereco.setMunicipio(municipio);

        if (Boolean.TRUE.equals(request.principal()))
            removerPrincipalExistente(pessoa);

        repository.save(endereco);

        return mapper.toResponse(endereco);
    }

    @Transactional
    public EnderecoResponse atualizar(Long id, EnderecoRequest request) {
        Endereco endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        Municipio municipio = municipioRepository.findById(request.municipioId())
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        if (Boolean.TRUE.equals(request.principal()))
            removerPrincipalExistente(pessoa);

        mapper.toEntity(request, pessoa, municipio);
        endereco.setPessoa(pessoa);
        endereco.setMunicipio(municipio);

        repository.save(endereco);

        return mapper.toResponse(endereco);
    }

    private void removerPrincipalExistente(Pessoa pessoa) {
        repository.findByPessoaAndPrincipalTrue(pessoa)
                .ifPresent(existing -> {
                    existing.setPrincipal(false);
                    repository.save(existing);
                });
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Endereço não encontrado.");

        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<EnderecoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-endereco-admin", fallbackMethod = "fallbackAdminListPessoaTipo")
    public List<EnderecoListDTO> listarPorPessoaETipo(Long pessoaId, String tipo) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoaAndTipo(pessoa, TipoEndereco.valueOf(tipo.toUpperCase()))
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorMunicipio(Long municipioId) {
        Municipio municipio = municipioRepository.findById(municipioId)
                .orElseThrow(() -> new EntityNotFoundException("Município não encontrado"));

        return repository.findByMunicipio(municipio)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnderecoListDTO> listarPorCep(String cep) {
        return repository.findByCep(cep)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<EnderecoResponse> buscarPrincipal(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoaAndPrincipalTrue(pessoa)
                .map(mapper::toResponse);
    }

}
