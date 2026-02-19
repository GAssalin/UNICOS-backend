package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.client.PermissaoClient;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusEstoque;
import br.com.unicos.ms_estoque.mapper.EstoqueMapper;
import br.com.unicos.ms_estoque.model.Estoque;
import br.com.unicos.ms_estoque.repository.EstoqueRepository;
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
 * Service responsável por regras de negócio e operações do agregado {@link Estoque}.
 */
@Service
@Transactional
public class EstoqueService extends BaseTenantService<Estoque, Long> {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;
    private final PermissaoClient permissaoClient;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            EstoqueMapper estoqueMapper,
            PermissaoClient permissaoClient
    ) {
        super(estoqueRepository);
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
        this.permissaoClient = permissaoClient;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdmin")
    public EstoqueResponseDto salvar(EstoqueCreateRequestDto request) {
        validarCodigoDuplicado(request.codigo());

        if (request.estoquePaiId() != null)
            // garante que o pai exista no tenant
            buscarEstoque(request.estoquePaiId());

        Estoque entity = estoqueMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId()); // tenant

        return estoqueMapper.toResponse(estoqueRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdminIdReq")
    public EstoqueResponseDto atualizar(Long id, EstoqueUpdateRequestDto request) {
        Estoque entity = buscarEstoque(id);

        if (!entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        if (request.estoquePaiId() != null) {
            if (id.equals(request.estoquePaiId()))
                throw new IllegalArgumentException("Um estoque não pode ser pai de si mesmo.");

            buscarEstoque(request.estoquePaiId());
        }

        estoqueMapper.updateEntity(request, entity);

        return estoqueMapper.toResponse(estoqueRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdminId")
    public EstoqueResponseDto buscarPorId(Long id) {
        return estoqueMapper.toResponse(buscarEstoque(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EstoqueResponseDto> listar(Pageable pageable) {
        return estoqueRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(estoqueMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdminPageStatus")
    public Page<EstoqueResponseDto> listarPorStatus(StatusEstoque status, Pageable pageable) {
        return estoqueRepository
                .findByStatusEstoqueAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(estoqueMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdminPagePai")
    public Page<EstoqueResponseDto> listarFilhos(Long estoquePaiId, Pageable pageable) {
        // garante que o pai exista no tenant
        buscarEstoque(estoquePaiId);

        return estoqueRepository
                .findByEstoquePaiIdAndEmpresaId(estoquePaiId, TenantContext.getEmpresaId(), pageable)
                .map(estoqueMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "estoque-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!permissaoClient.usuarioPossuiPermissao("DEPARTAMENTO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir estoques.");
        estoqueRepository.delete(buscarEstoque(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EstoqueResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private EstoqueResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private EstoqueResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<EstoqueResponseDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<EstoqueResponseDto> fallbackAdminPageStatus(StatusEstoque status, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private Page<EstoqueResponseDto> fallbackAdminPagePai(Long estoquePaiId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de estoques temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Estoque buscarEstoque(Long id) {
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado: " + id));

        // proteção adicional: garante tenant correto (caso findById não esteja tenant-aware)
        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao estoque fora do tenant.");

        return entity;
    }

    private void validarCodigoDuplicado(String codigo) {
        if (estoqueRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um estoque com o código informado neste tenant.");
    }
}
