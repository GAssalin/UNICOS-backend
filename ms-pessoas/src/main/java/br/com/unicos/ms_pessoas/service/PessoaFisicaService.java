package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.mapper.PessoaFisicaMapper;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import br.com.unicos.ms_pessoas.repository.PessoaFisicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio para cadastro e consulta
 * de Pessoas Físicas no UniCoS.
 */
@Service
public class PessoaFisicaService extends BaseTenantService<PessoaFisica, Long> {

    private final PessoaFisicaRepository repository;
    private final PessoaFisicaMapper mapper;

    public PessoaFisicaService(PessoaFisicaRepository repository, PessoaFisicaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public PessoaFisicaResponse criar(PessoaFisicaRequest request) {
        repository.findByCpf(request.cpf()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe uma pessoa física cadastrada com este CPF.");
        });

        PessoaFisica pessoa = mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    @Transactional
    public PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request) {
        PessoaFisica pessoa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada."));

        repository.findByCpf(request.cpf()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outra pessoa física com este CPF.");
        });

        mapper.toEntity(request);
        repository.save(pessoa);

        return mapper.toResponse(pessoa);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Pessoa Física não encontrada.");

        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<PessoaFisicaResponse> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarPorNomeSocial(String nomeSocial) {
        return repository.findByNomeSocial(nomeSocial)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaFisicaListDTO> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

}
