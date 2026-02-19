package br.com.unicos.ms_vendas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_vendas.dto.produtoatributovalor.ProdutoAtributoValorCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoatributovalor.ProdutoAtributoValorResponse;
import br.com.unicos.ms_vendas.dto.produtoatributovalor.ProdutoAtributoValorUpdateRequest;
import br.com.unicos.ms_vendas.mapper.ProdutoAtributoValorMapper;
import br.com.unicos.ms_vendas.model.ProdutoAtributoValor;
import br.com.unicos.ms_vendas.repository.ProdutoAtributoValorRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ProdutoAtributoValorService extends BaseTenantService<ProdutoAtributoValor, Long> {

    private final ProdutoAtributoValorRepository repository;
    private final ProdutoAtributoValorMapper mapper;

    public ProdutoAtributoValorService(
            ProdutoAtributoValorRepository repository,
            ProdutoAtributoValorMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoValorResponse criar(ProdutoAtributoValorCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarDuplicidade(request.produtoId(), request.atributoId(), empresaId);

        ProdutoAtributoValor valor = mapper.toEntity(request, empresaId);
        valor.setAtivo(true);

        return mapper.toResponse(repository.save(valor));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoValorResponse atualizar(Long id, ProdutoAtributoValorUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoAtributoValor valor = buscarValor(id, empresaId);

        mapper.updateEntity(request, valor);

        return mapper.toResponse(repository.save(valor));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoAtributoValorResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarValor(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdminList")
    public List<ProdutoAtributoValorResponse> listarPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByProdutoIdAndEmpresaId(produtoId, empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoAtributoValorResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarValor(id, empresaId));
    }

    @CircuitBreaker(name = "vendas-produto-atributo-valor-admin", fallbackMethod = "fallbackAdminVoid2")
    public void removerPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.deleteByProdutoIdAndEmpresaId(produtoId, empresaId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoAtributoValorResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de valores de atributos de produto temporariamente indisponível");
    }

    private Page<ProdutoAtributoValorResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de valores de atributos de produto temporariamente indisponível");
    }

    private List<ProdutoAtributoValorResponse> fallbackAdminList(Long produtoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de valores de atributos de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de valores de atributos de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid2(Long produtoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de valores de atributos de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoAtributoValor buscarValor(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Valor de atributo não encontrado: " + id));
    }

    private void validarDuplicidade(Long produtoId, Long atributoId, Long empresaId) {
        if (repository.existsByProdutoIdAndAtributoIdAndEmpresaId(produtoId, atributoId, empresaId))
            throw new IllegalArgumentException("Já existe um valor cadastrado para este atributo neste produto.");
    }
}
