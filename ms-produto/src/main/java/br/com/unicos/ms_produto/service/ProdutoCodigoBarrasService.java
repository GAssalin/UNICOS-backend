package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasCreateRequest;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasResponse;
import br.com.unicos.ms_produto.dto.produtocodigobarras.ProdutoCodigoBarrasUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoCodigoBarrasMapper;
import br.com.unicos.ms_produto.model.ProdutoCodigoBarras;
import br.com.unicos.ms_produto.repository.ProdutoCodigoBarrasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProdutoCodigoBarrasService extends BaseTenantService<ProdutoCodigoBarras, Long> {

    private final ProdutoCodigoBarrasRepository repository;
    private final ProdutoCodigoBarrasMapper mapper;

    public ProdutoCodigoBarrasService(ProdutoCodigoBarrasRepository repository, ProdutoCodigoBarrasMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoCodigoBarrasResponse criar(ProdutoCodigoBarrasCreateRequest request) {
        validarCodigoDuplicado(request.codigoBarras());

        if (request.principal())
            removerPrincipalAtual(request.produtoId());

        ProdutoCodigoBarras codigo = mapper.toEntity(request, TenantContext.getEmpresaId());
        codigo.setAtivo(true);

        return mapper.toResponse(repository.save(codigo));
    }

    public ProdutoCodigoBarrasResponse atualizar(Long id, ProdutoCodigoBarrasUpdateRequest request) {
        ProdutoCodigoBarras codigo = buscarCodigo(id);

        if (!codigo.getCodigoBarras().equalsIgnoreCase(request.codigoBarras()))
            validarCodigoDuplicado(request.codigoBarras());

        if (request.principal())
            removerPrincipalAtual(codigo.getProdutoId());

        mapper.updateEntity(request, codigo);

        return mapper.toResponse(repository.save(codigo));
    }

    @Transactional(readOnly = true)
    public ProdutoCodigoBarrasResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarCodigo(id));
    }

    @Transactional(readOnly = true)
    public ProdutoCodigoBarrasResponse buscarPorCodigoBarras(String codigoBarras) {
        ProdutoCodigoBarras codigo = repository.findByCodigoBarrasAndEmpresaId(codigoBarras, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Código de barras não encontrado: " + codigoBarras));

        return mapper.toResponse(codigo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoCodigoBarrasResponse> listarPorProduto(Long produtoId) {
        return repository.findByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void remover(Long id) {
        repository.delete(buscarCodigo(id));
    }

    public void removerPorProduto(Long produtoId) {
        repository.deleteByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId());
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoCodigoBarras buscarCodigo(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Código de barras não encontrado: " + id));
    }

    private void validarCodigoDuplicado(String codigoBarras) {
        if (repository.existsByCodigoBarrasAndEmpresaId(codigoBarras, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um código de barras cadastrado com o valor informado.");
    }

    private void removerPrincipalAtual(Long produtoId) {
        repository.findByProdutoIdAndPrincipalTrueAndEmpresaId(produtoId, TenantContext.getEmpresaId())
                .ifPresent(c -> {
                    c.setPrincipal(false);
                    repository.save(c);
                });
    }
}
