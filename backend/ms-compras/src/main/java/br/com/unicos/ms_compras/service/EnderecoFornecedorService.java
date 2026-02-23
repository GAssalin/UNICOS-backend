package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.EnderecoFornecedorDto;
import br.com.unicos.ms_compras.mapper.EnderecoFornecedorMapper;
import br.com.unicos.ms_compras.model.EnderecoFornecedor;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.repository.EnderecoFornecedorRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link EnderecoFornecedor}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamento com {@link Fornecedor} é resolvido no service
 * - Proteção tenant com métodos tenant-aware
 * - Regra: 1 endereço por tipo dentro do fornecedor/tenant
 * </p>
 */
@Service
@Transactional
public class EnderecoFornecedorService extends BaseTenantService<EnderecoFornecedor, Long> {

    private final EnderecoFornecedorRepository enderecoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final EnderecoFornecedorMapper enderecoMapper;

    public EnderecoFornecedorService(
            EnderecoFornecedorRepository enderecoRepository,
            FornecedorRepository fornecedorRepository,
            EnderecoFornecedorMapper enderecoMapper
    ) {
        super(enderecoRepository);
        this.enderecoRepository = enderecoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.enderecoMapper = enderecoMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdmin")
    public EnderecoFornecedorDto salvar(EnderecoFornecedorDto request) {
        if (request.fornecedorId() == null)
            throw new IllegalArgumentException("fornecedorId é obrigatório para criar um endereço.");

        Fornecedor fornecedor = buscarFornecedor(request.fornecedorId());

        validarTipoDuplicadoAoSalvar(fornecedor.getId(), request.tipo());

        EnderecoFornecedor entity = enderecoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setFornecedor(fornecedor);

        return enderecoMapper.toResponse(enderecoRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminIdReq")
    public EnderecoFornecedorDto atualizar(Long id, EnderecoFornecedorDto request) {
        EnderecoFornecedor entity = buscarEndereco(id);

        // não permite trocar fornecedor por este endpoint
        if (request.fornecedorId() != null
                && entity.getFornecedor() != null
                && !entity.getFornecedor().getId().equals(request.fornecedorId())) {
            throw new IllegalArgumentException("Não é permitido alterar fornecedorId deste endereço por este endpoint.");
        }

        Long fornecedorId = entity.getFornecedor().getId();

        // valida unicidade do tipo se mudou
        if (request.tipo() != null && !request.tipo().equalsIgnoreCase(entity.getTipo()))
            validarTipoDuplicadoAoAtualizar(id, fornecedorId, request.tipo());

        enderecoMapper.updateEntity(request, entity);

        return enderecoMapper.toResponse(enderecoRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminId")
    public EnderecoFornecedorDto buscarPorId(Long id) {
        return enderecoMapper.toResponse(buscarEndereco(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EnderecoFornecedorDto> listar(Pageable pageable) {
        return enderecoRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(enderecoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageFornecedor")
    public Page<EnderecoFornecedorDto> listarPorFornecedor(Long fornecedorId, Pageable pageable) {
        buscarFornecedor(fornecedorId);

        return enderecoRepository
                .findByFornecedorIdAndEmpresaId(fornecedorId, TenantContext.getEmpresaId(), pageable)
                .map(enderecoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminPageCep")
    public Page<EnderecoFornecedorDto> listarPorCep(String cep, Pageable pageable) {
        if (cep == null || cep.isBlank())
            throw new IllegalArgumentException("cep é obrigatório.");

        return enderecoRepository
                .findByCepAndEmpresaId(cep, TenantContext.getEmpresaId(), pageable)
                .map(enderecoMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        enderecoRepository.delete(buscarEndereco(id));
    }

    @CircuitBreaker(name = "compras-admin", fallbackMethod = "fallbackAdminVoidFornecedor")
    public void deletarPorFornecedor(Long fornecedorId) {
        buscarFornecedor(fornecedorId);

        enderecoRepository.deleteByFornecedorIdAndEmpresaId(
                fornecedorId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EnderecoFornecedorDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private EnderecoFornecedorDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private EnderecoFornecedorDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private Page<EnderecoFornecedorDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private Page<EnderecoFornecedorDto> fallbackAdminPageFornecedor(Long fornecedorId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private Page<EnderecoFornecedorDto> fallbackAdminPageCep(String cep, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    private void fallbackAdminVoidFornecedor(Long fornecedorId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de fornecedor temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EnderecoFornecedor buscarEndereco(Long id) {
        EnderecoFornecedor entity = enderecoRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço do fornecedor não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao endereço fora do tenant.");

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

    private void validarTipoDuplicadoAoSalvar(Long fornecedorId, String tipo) {
        if (tipo == null || tipo.isBlank())
            throw new IllegalArgumentException("tipo é obrigatório.");
        if (enderecoRepository.existsByFornecedorIdAndTipoAndEmpresaId(
                fornecedorId,
                tipo,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe endereço deste tipo para o fornecedor neste tenant.");
        }
    }

    private void validarTipoDuplicadoAoAtualizar(Long enderecoId, Long fornecedorId, String tipo) {
        enderecoRepository
                .findByFornecedorIdAndTipoAndEmpresaId(fornecedorId, tipo, TenantContext.getEmpresaId())
                .ifPresent(outro -> {
                    if (!outro.getId().equals(enderecoId))
                        throw new IllegalArgumentException("Já existe outro endereço deste tipo para o fornecedor neste tenant.");
                });
    }
}