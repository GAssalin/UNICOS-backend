package br.com.unicos.ms_pessoas.service;

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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Implementação das regras de negócio relacionadas aos documentos
 * vinculados a pessoas.
 */
@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final DocumentoMapper mapper;
    private final ModelMapper modelMapper;

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdmin")
    public DocumentoResponse criar(DocumentoRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        repository.findByNumero(request.numero()).ifPresent(doc -> {
            throw new IllegalArgumentException("Já existe um documento com este número.");
        });

        repository.findByPessoaAndTipo(pessoa, request.tipo()).ifPresent(doc -> {
            throw new IllegalArgumentException("A pessoa já possui um documento do tipo informado.");
        });

        Documento documento = mapper.toEntity(request);
        documento.setPessoa(pessoa);

        repository.save(documento);

        return mapper.toResponse(documento);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdmin")
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

        modelMapper.map(request, documento);
        documento.setPessoa(pessoa);

        repository.save(documento);

        return mapper.toResponse(documento);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdminVoid")
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Documento não encontrado");
        repository.deleteById(id);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdminOptional")
    public Optional<DocumentoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdminList")
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
    @CircuitBreaker(name = "pessoa-documento-admin", fallbackMethod = "fallbackAdminListTipo")
    public List<DocumentoListDTO> listarPorTipo(String tipo) {
        TipoDocumento tipoEnum;
        try {
            tipoEnum = TipoDocumento.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de documento inválido: " + tipo);
        }

        return repository.findByTipo(tipoEnum)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private DocumentoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos da pessoa temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos da pessoa temporariamente indisponível");
    }

    private Optional<DocumentoResponse> fallbackAdminOptional(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos da pessoa temporariamente indisponível");
    }

    private List<DocumentoListDTO> fallbackAdminList(Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos da pessoa temporariamente indisponível");
    }

    private List<DocumentoListDTO> fallbackAdminListPessoa(Long pessoaId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos da pessoa temporariamente indisponível");
    }

    private List<DocumentoListDTO> fallbackAdminListTipo(String tipo, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos da pessoa temporariamente indisponível");
    }
}
