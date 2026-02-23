package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.ContatoFornecedorDto;
import br.com.unicos.ms_compras.mapper.ContatoFornecedorMapper;
import br.com.unicos.ms_compras.model.ContatoFornecedor;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.repository.ContatoFornecedorRepository;
import br.com.unicos.ms_compras.repository.FornecedorRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link ContatoFornecedor}.
 *
 * <p>
 * Padrões UniCoS aplicados:
 * - Relacionamento com {@link Fornecedor} é resolvido no service
 * - Proteção tenant ao buscar entidades (preferindo métodos tenant-aware do repository)
 * - Regra: apenas 1 contato "principal" por fornecedor (no tenant)
 * </p>
 */
@Service
@Transactional
public class ContatoFornecedorService extends BaseTenantService<ContatoFornecedor, Long> {

    private final ContatoFornecedorRepository contatoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ContatoFornecedorMapper contatoMapper;

    public ContatoFornecedorService(
            ContatoFornecedorRepository contatoRepository,
            FornecedorRepository fornecedorRepository,
            ContatoFornecedorMapper contatoMapper
    ) {
        super(contatoRepository);
        this.contatoRepository = contatoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.contatoMapper = contatoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdmin")
    public ContatoFornecedorDto salvar(ContatoFornecedorDto request) {
        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório para criar um contato.");

        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        // regra: se marcar principal e já existir outro principal, bloqueia (ou você pode optar por "desmarcar o anterior")
        validarPrincipalUnicoAoSalvar(fornecedor.getId(), request.principal());

        ContatoFornecedor entity = contatoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setFornecedor(fornecedor);

        ContatoFornecedor salvo = contatoRepository.save(entity);

        return contatoMapper.toResponse(salvo);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminIdReq")
    public ContatoFornecedorDto atualizar(Long id, ContatoFornecedorDto request) {
        ContatoFornecedor entity = buscarContato(id);

        // não permite trocar fornecedor por este endpoint (conforme mapper doc)
        if (request.fornecedorId() != null
                && entity.getFornecedor() != null
                && !entity.getFornecedor().getId().equals(request.fornecedorId())) {
            throw new IllegalArgumentException("Não é permitido alterar fornecedorId deste contato por este endpoint.");
        }

        Long fornecedorId = entity.getFornecedor().getId();

        // se está tentando marcar principal, valida regra de unicidade (desconsiderando ele mesmo)
        validarPrincipalUnicoAoAtualizar(fornecedorId, id, request.principal());

        contatoMapper.updateEntity(request, entity);

        ContatoFornecedor atualizado = contatoRepository.save(entity);

        return contatoMapper.toResponse(atualizado);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminId")
    public ContatoFornecedorDto buscarPorId(Long id) {
        return contatoMapper.toResponse(buscarContato(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ContatoFornecedorDto> listar(Pageable pageable) {
        return contatoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(contatoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageFornecedor")
    public Page<ContatoFornecedorDto> listarPorFornecedor(Long fornecedorId, Pageable pageable) {
        buscarFornecedor(fornecedorId);

        return contatoRepository
                .findByFornecedorIdAndEmpresaId(fornecedorId, TenantContext.getEmpresaId(), pageable)
                .map(contatoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPrincipal")
    public ContatoFornecedorDto buscarPrincipal(Long fornecedorId) {
        buscarFornecedor(fornecedorId);

        ContatoFornecedor principal = contatoRepository
                .findByFornecedorIdAndPrincipalTrueAndEmpresaId(fornecedorId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não possui contato principal cadastrado. FornecedorId: " + fornecedorId));

        return contatoMapper.toResponse(principal);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        contatoRepository.delete(buscarContato(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ContatoFornecedorDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    private ContatoFornecedorDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    private ContatoFornecedorDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    private Page<ContatoFornecedorDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    private Page<ContatoFornecedorDto> fallbackAdminPageFornecedor(Long fornecedorId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    private ContatoFornecedorDto fallbackAdminPrincipal(Long fornecedorId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de fornecedor temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ContatoFornecedor buscarContato(Long id) {
        // preferindo tenant-aware do repository
        ContatoFornecedor entity = contatoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Contato do fornecedor não encontrado: " + id));

        // proteção adicional (caso alguém troque o método no futuro)
        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao contato fora do tenant.");

        return entity;
    }

    private Fornecedor buscarFornecedor(Long id) {
        Fornecedor entity = fornecedorRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao fornecedor fora do tenant.");

        return entity;
    }

    private void validarPrincipalUnicoAoSalvar(Long fornecedorId, Boolean principal) {
        if (!Boolean.TRUE.equals(principal)) return;

        if (contatoRepository.existsByFornecedorIdAndPrincipalTrueAndEmpresaId(fornecedorId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um contato principal para este fornecedor neste tenant.");
    }

    private void validarPrincipalUnicoAoAtualizar(Long fornecedorId, Long contatoId, Boolean principal) {
        if (!Boolean.TRUE.equals(principal)) return;

        contatoRepository.findByFornecedorIdAndPrincipalTrueAndEmpresaId(fornecedorId, TenantContext.getEmpresaId())
                .ifPresent(principalAtual -> {
                    if (!principalAtual.getId().equals(contatoId))
                        throw new IllegalArgumentException("Já existe outro contato principal para este fornecedor neste tenant.");
                });
    }
}