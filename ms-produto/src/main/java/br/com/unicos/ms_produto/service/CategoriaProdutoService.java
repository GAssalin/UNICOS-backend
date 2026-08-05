package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoUpdateRequest;
import br.com.unicos.ms_produto.mapper.CategoriaProdutoMapper;
import br.com.unicos.ms_produto.model.CategoriaProduto;
import br.com.unicos.ms_produto.repository.CategoriaProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoriaProdutoService extends BaseTenantService<CategoriaProduto, Long> {

    private final CategoriaProdutoRepository repository;
    private final CategoriaProdutoMapper mapper;

    public CategoriaProdutoService(CategoriaProdutoRepository repository, CategoriaProdutoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public CategoriaProdutoResponse criar(CategoriaProdutoCreateRequest request) {
        validarNomeDuplicado(request.nome());

        CategoriaProduto categoria = mapper.toEntity(request, TenantContext.getEmpresaId());
        categoria.setAtivo(true);

        return mapper.toResponse(repository.save(categoria));
    }

    public CategoriaProdutoResponse atualizar(Long id, CategoriaProdutoUpdateRequest request) {
        CategoriaProduto categoria = buscarCategoria(id);

        if (!categoria.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome());

        mapper.updateEntity(request, categoria);

        return mapper.toResponse(repository.save(categoria));
    }

    @Transactional(readOnly = true)
    public CategoriaProdutoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarCategoria(id));
    }

    @Transactional(readOnly = true)
    public Page<CategoriaProdutoResumoResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<CategoriaProdutoResumoResponse> listarPorCategoriaPai(Long categoriaPaiId, Pageable pageable) {
        return repository.findByCategoriaPaiIdAndEmpresaId(categoriaPaiId, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public CategoriaProdutoResponse ativar(Long id) {
        CategoriaProduto categoria = buscarCategoria(id);
        categoria.setAtivo(true);

        return mapper.toResponse(repository.save(categoria));
    }

    public CategoriaProdutoResponse inativar(Long id) {
        CategoriaProduto categoria = buscarCategoria(id);
        categoria.setAtivo(false);

        return mapper.toResponse(repository.save(categoria));
    }

    public void remover(Long id) {
        repository.delete(buscarCategoria(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private CategoriaProduto buscarCategoria(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria de produto não encontrada: " + id));
    }

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeAndEmpresaId(nome, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma categoria de produto com o nome informado.");
    }
}
