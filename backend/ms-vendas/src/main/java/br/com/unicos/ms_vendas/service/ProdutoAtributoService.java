package br.com.unicos.ms_vendas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoResponse;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoResumoResponse;
import br.com.unicos.ms_vendas.dto.produtoatributo.ProdutoAtributoUpdateRequest;
import br.com.unicos.ms_vendas.mapper.ProdutoAtributoMapper;
import br.com.unicos.ms_vendas.model.ProdutoAtributo;
import br.com.unicos.ms_vendas.repository.ProdutoAtributoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProdutoAtributoService extends BaseTenantService<ProdutoAtributo, Long> {

    private final ProdutoAtributoRepository repository;
    private final ProdutoAtributoMapper mapper;

    public ProdutoAtributoService(
            ProdutoAtributoRepository repository,
            ProdutoAtributoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoResponse criar(ProdutoAtributoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarNomeDuplicado(request.nome(), empresaId);

        ProdutoAtributo atributo = mapper.toEntity(request, empresaId);
        atributo.setAtivo(true);

        return mapper.toResponse(repository.save(atributo));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoResponse atualizar(Long id, ProdutoAtributoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoAtributo atributo = buscarAtributo(id, empresaId);

        if (!atributo.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome(), empresaId);

        mapper.updateEntity(request, atributo);

        return mapper.toResponse(repository.save(atributo));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarAtributo(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoAtributoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoAtributoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByAtivoAndEmpresaId(ativo, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // STATUS
    // ============================================================

    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoResponse ativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoAtributo atributo = buscarAtributo(id, empresaId);
        atributo.setAtivo(true);

        return mapper.toResponse(repository.save(atributo));
    }

    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoResponse inativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoAtributo atributo = buscarAtributo(id, empresaId);
        atributo.setAtivo(false);

        return mapper.toResponse(repository.save(atributo));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "produto-atributo-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarAtributo(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoAtributoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de atributos de produto temporariamente indisponível");
    }

    private Page<ProdutoAtributoResumoResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de atributos de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de atributos de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoAtributo buscarAtributo(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Atributo de produto não encontrado: " + id));
    }

    private void validarNomeDuplicado(String nome, Long empresaId) {
        if (repository.existsByNomeAndEmpresaId(nome, empresaId))
            throw new IllegalArgumentException("Já existe um atributo de produto com o nome informado.");
    }
}
