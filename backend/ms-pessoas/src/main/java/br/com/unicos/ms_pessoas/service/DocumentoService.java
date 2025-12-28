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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // Criar
    // ============================================================
    @Transactional
    public DocumentoResponse criar(DocumentoRequest request) {
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        // Impede duplicação de número de documento
        repository.findByNumero(request.numero()).ifPresent(doc -> {
            throw new IllegalArgumentException("Já existe um documento com este número.");
        });

        // Impede duplicação de tipo para a mesma pessoa (ex.: dois CPFs)
        repository.findByPessoaAndTipo(pessoa, request.tipo()).ifPresent(doc -> {
            throw new IllegalArgumentException("A pessoa já possui um documento do tipo informado.");
        });

        Documento documento = mapper.toEntity(request);
        documento.setPessoa(pessoa);

        repository.save(documento);

        return mapper.toResponse(documento);
    }

    // ============================================================
    // Atualizar
    // ============================================================
    @Transactional
    public DocumentoResponse atualizar(Long id, DocumentoRequest request) {
        Documento documento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        // Valida duplicação de número para outro registro
        repository.findByNumero(request.numero()).ifPresent(existing -> {
            if (!existing.getId().equals(id))
                throw new IllegalArgumentException("Já existe outro documento com este número.");
        });

        // Valida duplicação de tipo para a mesma pessoa
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
    // Excluir
    // ============================================================
    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Documento não encontrado");
        repository.deleteById(id);
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @Transactional(readOnly = true)
    public Optional<DocumentoResponse> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse);
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Pessoa
    // ============================================================

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarPorPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return repository.findByPessoa(pessoa)
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // Listar por Tipo
    // ============================================================

    @Transactional(readOnly = true)
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
}
