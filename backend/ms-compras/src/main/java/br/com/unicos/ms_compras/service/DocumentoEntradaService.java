package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.DocumentoEntradaDto;
import br.com.unicos.ms_compras.mapper.DocumentoEntradaMapper;
import br.com.unicos.ms_compras.model.DocumentoEntrada;
import br.com.unicos.ms_compras.model.RecebimentoCompra;
import br.com.unicos.ms_compras.repository.DocumentoEntradaRepository;
import br.com.unicos.ms_compras.repository.RecebimentoCompraRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsável por regras de negócio e operações do agregado {@link DocumentoEntrada}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamento com {@link RecebimentoCompra} é resolvido no service
 * - Proteção tenant usando métodos tenant-aware do repository
 * - Valida unicidade por tenant: (tipoDocumento + numero)
 * - (Opcional) valida unicidade por tenant: chaveAcesso, quando informada
 * </p>
 */
@Service
@Transactional
public class DocumentoEntradaService extends BaseTenantService<DocumentoEntrada, Long> {

    private final DocumentoEntradaRepository documentoRepository;
    private final DocumentoEntradaMapper documentoMapper;
    private final RecebimentoCompraRepository recebimentoCompraRepository;

    public DocumentoEntradaService(
            DocumentoEntradaRepository documentoRepository,
            DocumentoEntradaMapper documentoMapper,
            RecebimentoCompraRepository recebimentoCompraRepository
    ) {
        super(documentoRepository);
        this.documentoRepository = documentoRepository;
        this.documentoMapper = documentoMapper;
        this.recebimentoCompraRepository = recebimentoCompraRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdmin")
    public DocumentoEntradaDto salvar(DocumentoEntradaDto request) {
        if (request.recebimentoCompraId() == null)
            throw new IllegalArgumentException("recebimentoCompraId é obrigatório para criar um documento de entrada.");

        RecebimentoCompra recebimento = buscarRecebimentoCompra(request.recebimentoCompraId());

        validarDuplicidadeTipoNumeroAoSalvar(request.tipoDocumento(), request.numero());
        validarDuplicidadeChaveAcessoAoSalvar(request.chaveAcesso());

        DocumentoEntrada entity = documentoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setRecebimentoCompra(recebimento);

        return documentoMapper.toResponse(documentoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminIdReq")
    public DocumentoEntradaDto atualizar(Long id, DocumentoEntradaDto request) {
        DocumentoEntrada entity = buscarDocumento(id);

        // não permite trocar vínculo com recebimento por este endpoint
        if (request.recebimentoCompraId() != null
                && entity.getRecebimentoCompra() != null
                && !entity.getRecebimentoCompra().getId().equals(request.recebimentoCompraId())) {
            throw new IllegalArgumentException("Não é permitido alterar recebimentoCompraId deste documento por este endpoint.");
        }

        // unicidade (tipoDocumento + numero) — valida apenas se mudou
        if (mudouTipoOuNumero(entity, request))
            validarDuplicidadeTipoNumeroAoAtualizar(id, request.tipoDocumento(), request.numero());

        // unicidade chaveAcesso (se informada) — valida apenas se mudou
        if (mudouChaveAcesso(entity, request))
            validarDuplicidadeChaveAcessoAoAtualizar(id, request.chaveAcesso());

        documentoMapper.updateEntity(request, entity);

        return documentoMapper.toResponse(documentoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminId")
    public DocumentoEntradaDto buscarPorId(Long id) {
        return documentoMapper.toResponse(buscarDocumento(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPage")
    public Page<DocumentoEntradaDto> listar(Pageable pageable) {
        return documentoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(documentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageRecebimento")
    public Page<DocumentoEntradaDto> listarPorRecebimento(Long recebimentoCompraId, Pageable pageable) {
        buscarRecebimentoCompra(recebimentoCompraId);

        return documentoRepository
                .findByRecebimentoCompraIdAndEmpresaId(recebimentoCompraId, TenantContext.getEmpresaId(), pageable)
                .map(documentoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        documentoRepository.delete(buscarDocumento(id));
    }

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoidRecebimento")
    public void deletarPorRecebimento(Long recebimentoCompraId) {
        buscarRecebimentoCompra(recebimentoCompraId);

        documentoRepository.deleteByRecebimentoCompraIdAndEmpresaId(
                recebimentoCompraId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private DocumentoEntradaDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    private DocumentoEntradaDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    private DocumentoEntradaDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    private Page<DocumentoEntradaDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    private Page<DocumentoEntradaDto> fallbackAdminPageRecebimento(Long recebimentoCompraId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    private void fallbackAdminVoidRecebimento(Long recebimentoCompraId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de documentos de entrada temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private DocumentoEntrada buscarDocumento(Long id) {
        DocumentoEntrada entity = documentoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Documento de entrada não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao documento fora do tenant.");

        return entity;
    }

    private RecebimentoCompra buscarRecebimentoCompra(Long id) {
        // Ajuste para tenant-aware se seu RecebimentoCompraRepository tiver findByIdAndEmpresaId(...)
        RecebimentoCompra recebimento = recebimentoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecebimentoCompra não encontrado: " + id));

        if (recebimento.getEmpresaId() != null && !recebimento.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao recebimento fora do tenant.");

        return recebimento;
    }

    private void validarDuplicidadeTipoNumeroAoSalvar(String tipoDocumento, String numero) {
        if (tipoDocumento == null || tipoDocumento.isBlank())
            throw new IllegalArgumentException("tipoDocumento é obrigatório.");
        if (numero == null || numero.isBlank())
            throw new IllegalArgumentException("numero é obrigatório.");
        if (documentoRepository.existsByTipoDocumentoAndNumeroAndEmpresaId(tipoDocumento, numero, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um documento com o mesmo tipo e número neste tenant.");
    }

    private void validarDuplicidadeTipoNumeroAoAtualizar(Long documentoId, String tipoDocumento, String numero) {
        documentoRepository.findByTipoDocumentoAndNumeroAndEmpresaId(tipoDocumento, numero, TenantContext.getEmpresaId())
                .ifPresent(outro -> {
                    if (!outro.getId().equals(documentoId))
                        throw new IllegalArgumentException("Já existe outro documento com o mesmo tipo e número neste tenant.");
                });
    }

    private void validarDuplicidadeChaveAcessoAoSalvar(String chaveAcesso) {
        if (chaveAcesso == null || chaveAcesso.isBlank()) return;

        documentoRepository.findByChaveAcessoAndEmpresaId(chaveAcesso, TenantContext.getEmpresaId())
                .ifPresent(d -> {
                    throw new IllegalArgumentException("Já existe um documento com a mesma chave de acesso neste tenant.");
                });
    }

    private void validarDuplicidadeChaveAcessoAoAtualizar(Long documentoId, String chaveAcesso) {
        if (chaveAcesso == null || chaveAcesso.isBlank()) return;

        documentoRepository.findByChaveAcessoAndEmpresaId(chaveAcesso, TenantContext.getEmpresaId())
                .ifPresent(outro -> {
                    if (!outro.getId().equals(documentoId))
                        throw new IllegalArgumentException("Já existe outro documento com a mesma chave de acesso neste tenant.");
                });
    }

    private boolean mudouTipoOuNumero(DocumentoEntrada entity, DocumentoEntradaDto dto) {
        if (dto == null) return false;
        boolean mudouTipo = dto.tipoDocumento() != null && !dto.tipoDocumento().equalsIgnoreCase(entity.getTipoDocumento());
        boolean mudouNumero = dto.numero() != null && !dto.numero().equalsIgnoreCase(entity.getNumero());
        return mudouTipo || mudouNumero;
    }

    private boolean mudouChaveAcesso(DocumentoEntrada entity, DocumentoEntradaDto dto) {
        if (dto == null) return false;

        String atual = entity.getChaveAcesso();
        String nova = dto.chaveAcesso();

        if (atual == null && nova == null) return false;
        if (atual == null) return !nova.isBlank();
        if (nova == null) return !atual.isBlank();

        return !atual.equalsIgnoreCase(nova);
    }
}