package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.FornecedorDto;
import br.com.unicos.ms_compras.mapper.FornecedorMapper;
import br.com.unicos.ms_compras.model.Fornecedor;
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
 * Service responsável por regras de negócio e operações do agregado {@link Fornecedor}.
 *
 * <p>
 * Regras aplicadas:
 * - Unicidade de código por tenant
 * - Unicidade de CNPJ por tenant
 * - Proteção multi-tenant (empresaId)
 * </p>
 */
@Service
@Transactional
public class FornecedorService extends BaseTenantService<Fornecedor, Long> {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    public FornecedorService(
            FornecedorRepository fornecedorRepository,
            FornecedorMapper fornecedorMapper
    ) {
        super(fornecedorRepository);
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorMapper = fornecedorMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdmin")
    public FornecedorDto salvar(FornecedorDto request) {
        validarCodigoDuplicado(request.codigo());
        validarCnpjDuplicado(request.cnpj());

        Fornecedor entity = fornecedorMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return fornecedorMapper.toResponse(fornecedorRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminIdReq")
    public FornecedorDto atualizar(Long id, FornecedorDto request) {
        Fornecedor entity = buscarFornecedor(id);

        // valida código se mudou
        if (!entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        // valida CNPJ se mudou
        if (!entity.getCnpj().equalsIgnoreCase(request.cnpj()))
            validarCnpjDuplicado(request.cnpj());

        fornecedorMapper.updateEntity(request, entity);

        return fornecedorMapper.toResponse(fornecedorRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminId")
    public FornecedorDto buscarPorId(Long id) {
        return fornecedorMapper.toResponse(buscarFornecedor(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminCodigo")
    public FornecedorDto buscarPorCodigo(String codigo) {
        Fornecedor entity = fornecedorRepository
                .findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado para o código: " + codigo));

        return fornecedorMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminCnpj")
    public FornecedorDto buscarPorCnpj(String cnpj) {
        Fornecedor entity = fornecedorRepository
                .findByCnpjAndEmpresaId(cnpj, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado para o CNPJ: " + cnpj));

        return fornecedorMapper.toResponse(entity);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminPage")
    public Page<FornecedorDto> listar(Pageable pageable) {
        return fornecedorRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(fornecedorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminPageRazao")
    public Page<FornecedorDto> pesquisarPorRazaoSocial(String razaoSocial, Pageable pageable) {
        return fornecedorRepository
                .findByRazaoSocialContainingIgnoreCaseAndEmpresaId(razaoSocial, TenantContext.getEmpresaId(), pageable)
                .map(fornecedorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminPageFantasia")
    public Page<FornecedorDto> pesquisarPorNomeFantasia(String nomeFantasia, Pageable pageable) {
        return fornecedorRepository
                .findByNomeFantasiaContainingIgnoreCaseAndEmpresaId(nomeFantasia, TenantContext.getEmpresaId(), pageable)
                .map(fornecedorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminPageCnpj")
    public Page<FornecedorDto> pesquisarPorCnpj(String cnpj, Pageable pageable) {
        return fornecedorRepository
                .findByCnpjContainingAndEmpresaId(cnpj, TenantContext.getEmpresaId(), pageable)
                .map(fornecedorMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-fornecedor-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        fornecedorRepository.delete(buscarFornecedor(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private FornecedorDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de fornecedores temporariamente indisponível");
    }

    private FornecedorDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de fornecedores temporariamente indisponível");
    }

    private FornecedorDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de fornecedores temporariamente indisponível");
    }

    private Page<FornecedorDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de fornecedores temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de fornecedores temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Fornecedor buscarFornecedor(Long id) {
        Fornecedor entity = fornecedorRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao fornecedor fora do tenant.");

        return entity;
    }

    private void validarCodigoDuplicado(String codigo) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("codigo é obrigatório.");
        if (fornecedorRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe fornecedor com o código informado neste tenant.");
    }

    private void validarCnpjDuplicado(String cnpj) {
        if (cnpj == null || cnpj.isBlank())
            throw new IllegalArgumentException("cnpj é obrigatório.");
        if (fornecedorRepository.existsByCnpjAndEmpresaId(cnpj, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe fornecedor com o CNPJ informado neste tenant.");
    }
}