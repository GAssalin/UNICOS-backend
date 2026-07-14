package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import br.com.unicos.ms_pessoas.mapper.DocumentoMapper;
import br.com.unicos.ms_pessoas.model.Documento;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.repository.DocumentoRepository;
import br.com.unicos.ms_pessoas.repository.PessoaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio relacionadas aos documentos
 * vinculados a pessoas.
 */
@Service
public class DocumentoService extends BaseTenantService<Documento, Long> {

    private final DocumentoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final DocumentoMapper mapper;

    public DocumentoService(DocumentoRepository repository, PessoaRepository pessoaRepository, DocumentoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public DocumentoResponse criar(DocumentoRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        repository.findByNumero(request.numero()).ifPresent(doc -> {
            throw new IllegalArgumentException("Já existe um documento com este número.");
        });

        repository.findByPessoaAndTipo(pessoa, request.tipo()).ifPresent(doc -> {
            throw new IllegalArgumentException("A pessoa já possui um documento do tipo informado.");
        });

        Documento documento = mapper.toEntity(request, pessoa);
        documento.setPessoa(pessoa);

        repository.save(documento);

        return mapper.toResponse(documento);
    }

    @Transactional
    public DocumentoResponse atualizar(Long id, DocumentoRequest request) {
        Documento documento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        repository.findByNumero(request.numero()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro documento com este número.");
        });

        repository.findByPessoaAndTipo(pessoa, request.tipo()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("A pessoa já possui outro documento deste tipo.");
        });

        mapper.toEntity(request, pessoa);
        documento.setPessoa(pessoa);

        repository.save(documento);

        return mapper.toResponse(documento);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Documento não encontrado");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<DocumentoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdminListPessoa")
    public List<DocumentoListDTO> listarPorPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarPorTipo(String tipo) {
        return repository.findByTipo(TipoDocumento.valueOf(tipo.toUpperCase()))
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

}
