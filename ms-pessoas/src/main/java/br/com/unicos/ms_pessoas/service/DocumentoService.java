package br.com.unicos.ms_pessoas.service;

import br.com.unicos.core.tenant.context.TenantContext;
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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Regras de negócio aplicadas aos documentos das pessoas.
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
        Pessoa pessoa = buscarPessoa(request.pessoaId());

        validarDuplicidade(request, pessoa, null);

        Documento documento = mapper.toEntity(request, pessoa);
        documento.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(documento));
    }

    @Transactional
    public DocumentoResponse atualizar(Long id, DocumentoRequest request) {
        Documento documento = buscarDocumento(id);
        Pessoa pessoa = buscarPessoa(request.pessoaId());

        validarDuplicidade(request, pessoa, id);
        mapper.updateEntity(documento, request, pessoa);

        return mapper.toResponse(repository.save(documento));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarDocumento(id));
    }

    @Transactional(readOnly = true)
    public Optional<DocumentoResponse> buscarPorId(Long id) {
        return findById(id)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarTodos() {
        return repository.findByEmpresaId(TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarPorPessoa(Long pessoaId) {
        return repository.findByPessoaAndEmpresaId(buscarPessoa(pessoaId), TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentoListDTO> listarPorTipo(String tipo) {
        return repository.findByTipoAndEmpresaId(converterTipo(tipo), TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Documento buscarDocumento(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado."));
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findByIdAndEmpresaId(pessoaId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
    }

    /**
     * O número do documento é único em toda a base (restrição do banco); o tipo é único por pessoa.
     */
    private void validarDuplicidade(DocumentoRequest request, Pessoa pessoa, Long idAtual) {
        repository.findByNumero(request.numero()).ifPresent(existing -> {
            if (!existing.getId().equals(idAtual))
                throw new IllegalArgumentException("Já existe um documento com este número.");
        });

        repository.findByPessoaAndTipoAndEmpresaId(pessoa, request.tipo(), TenantContext.getEmpresaId()).ifPresent(existing -> {
            if (!existing.getId().equals(idAtual))
                throw new IllegalArgumentException("A pessoa já possui um documento do tipo informado.");
        });
    }

    private static TipoDocumento converterTipo(String tipo) {
        try {
            return TipoDocumento.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("Tipo de documento inválido: " + tipo);
        }
    }
}
