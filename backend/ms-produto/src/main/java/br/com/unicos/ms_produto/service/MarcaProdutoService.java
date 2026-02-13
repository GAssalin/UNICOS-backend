package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoResponse;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.marca.MarcaProdutoUpdateRequest;
import br.com.unicos.ms_produto.mapper.MarcaProdutoMapper;
import br.com.unicos.ms_produto.model.MarcaProduto;
import br.com.unicos.ms_produto.repository.MarcaProdutoRepository;
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
public class MarcaProdutoService extends BaseTenantService<MarcaProduto, Long> {

    private final MarcaProdutoRepository repository;
    private final MarcaProdutoMapper mapper;

    public MarcaProdutoService(
            MarcaProdutoRepository repository,
            MarcaProdutoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdmin")
    public MarcaProdutoResponse criar(MarcaProdutoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarNomeDuplicado(request.nome(), empresaId);

        MarcaProduto marca = mapper.toEntity(request, empresaId);
        marca.setAtivo(true);

        return mapper.toResponse(repository.save(marca));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdmin")
    public MarcaProdutoResponse atualizar(Long id, MarcaProdutoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        MarcaProduto marca = buscarMarca(id, empresaId);

        if (!marca.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome(), empresaId);

        mapper.updateEntity(request, marca);

        return mapper.toResponse(repository.save(marca));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdmin")
    public MarcaProdutoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarMarca(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<MarcaProdutoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<MarcaProdutoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByAtivoAndEmpresaId(ativo, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // STATUS
    // ============================================================

    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdmin")
    public MarcaProdutoResponse ativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        MarcaProduto marca = buscarMarca(id, empresaId);
        marca.setAtivo(true);

        return mapper.toResponse(repository.save(marca));
    }

    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdmin")
    public MarcaProdutoResponse inativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        MarcaProduto marca = buscarMarca(id, empresaId);
        marca.setAtivo(false);

        return mapper.toResponse(repository.save(marca));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "marca-produto-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarMarca(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private MarcaProdutoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de marcas de produto temporariamente indisponível");
    }

    private Page<MarcaProdutoResumoResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de marcas de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de marcas de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private MarcaProduto buscarMarca(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Marca de produto não encontrada: " + id));
    }

    private void validarNomeDuplicado(String nome, Long empresaId) {
        if (repository.existsByNomeAndEmpresaId(nome, empresaId))
            throw new IllegalArgumentException("Já existe uma marca de produto com o nome informado.");
    }
}
