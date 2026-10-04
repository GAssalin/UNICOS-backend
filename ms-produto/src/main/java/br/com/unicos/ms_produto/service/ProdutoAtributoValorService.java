package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorCreateRequest;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorResponse;
import br.com.unicos.ms_produto.dto.produtoatributovalor.ProdutoAtributoValorUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoAtributoValorMapper;
import br.com.unicos.ms_produto.model.ProdutoAtributoValor;
import br.com.unicos.ms_produto.repository.ProdutoAtributoRepository;
import br.com.unicos.ms_produto.repository.ProdutoAtributoValorRepository;
import br.com.unicos.ms_produto.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProdutoAtributoValorService extends BaseTenantService<ProdutoAtributoValor, Long> {

    private final ProdutoAtributoValorRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoAtributoRepository atributoRepository;
    private final ProdutoAtributoValorMapper mapper;

    public ProdutoAtributoValorService(ProdutoAtributoValorRepository repository, ProdutoRepository produtoRepository, ProdutoAtributoRepository atributoRepository, ProdutoAtributoValorMapper mapper) {
        super(repository);
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.atributoRepository = atributoRepository;
        this.mapper = mapper;
    }

    public ProdutoAtributoValorResponse criar(ProdutoAtributoValorCreateRequest request) {
        validarProduto(request.produtoId());
        validarAtributo(request.atributoId());

        validarDuplicidade(request.produtoId(), request.atributoId());

        ProdutoAtributoValor valor = mapper.toEntity(request, TenantContext.getEmpresaId());
        valor.setAtivo(true);

        return mapper.toResponse(repository.save(valor));
    }

    public ProdutoAtributoValorResponse atualizar(Long id, ProdutoAtributoValorUpdateRequest request) {
        ProdutoAtributoValor valor = buscarValor(id);

        mapper.updateEntity(valor, request);

        return mapper.toResponse(repository.save(valor));
    }

    @Transactional(readOnly = true)
    public ProdutoAtributoValorResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarValor(id));
    }

    @Transactional(readOnly = true)
    public List<ProdutoAtributoValorResponse> listarPorProduto(Long produtoId) {
        return repository.findByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProdutoAtributoValorResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    public void remover(Long id) {
        repository.delete(buscarValor(id));
    }

    public void removerPorProduto(Long produtoId) {
        repository.deleteByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId());
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoAtributoValor buscarValor(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Valor de atributo não encontrado: " + id));
    }

    private void validarDuplicidade(Long produtoId, Long atributoId) {
        if (repository.existsByProdutoIdAndAtributoIdAndEmpresaId(produtoId, atributoId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um valor cadastrado para este atributo neste produto.");
    }

    private void validarAtributo(Long atributoId) {
        if (!atributoRepository.existsByIdAndEmpresaId(atributoId, TenantContext.getEmpresaId()))
            throw new EntityNotFoundException("Atributo de produto não encontrado: " + atributoId);
    }

    private void validarProduto(Long produtoId) {
        if (!produtoRepository.existsByIdAndEmpresaId(produtoId, TenantContext.getEmpresaId()))
            throw new EntityNotFoundException("Produto não encontrado: " + produtoId);
    }
}
