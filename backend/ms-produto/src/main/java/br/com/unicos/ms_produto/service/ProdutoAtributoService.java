package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoCreateRequest;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoResponse;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoResumoResponse;
import br.com.unicos.ms_produto.dto.produtoatributo.ProdutoAtributoUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoAtributoMapper;
import br.com.unicos.ms_produto.model.ProdutoAtributo;
import br.com.unicos.ms_produto.repository.ProdutoAtributoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoAtributoService extends BaseTenantService<ProdutoAtributo, Long> {

    private final ProdutoAtributoRepository repository;
    private final ProdutoAtributoMapper mapper;

    public ProdutoAtributoService(ProdutoAtributoRepository repository, ProdutoAtributoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoAtributoResponse criar(ProdutoAtributoCreateRequest request) {
        validarNomeDuplicado(request.nome());

        ProdutoAtributo atributo = mapper.toEntity(request, TenantContext.getEmpresaId());
        atributo.setAtivo(true);

        return mapper.toResponse(repository.save(atributo));
    }

    public ProdutoAtributoResponse atualizar(Long id, ProdutoAtributoUpdateRequest request) {
        ProdutoAtributo atributo = buscarAtributo(id);

        if (!atributo.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome());

        mapper.updateEntity(request, atributo);

        return mapper.toResponse(repository.save(atributo));
    }

    @Transactional(readOnly = true)
    public ProdutoAtributoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarAtributo(id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoAtributoResumoResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoAtributoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public ProdutoAtributoResponse ativar(Long id) {
        ProdutoAtributo atributo = buscarAtributo(id);
        atributo.setAtivo(true);

        return mapper.toResponse(repository.save(atributo));
    }

    public ProdutoAtributoResponse inativar(Long id) {
        ProdutoAtributo atributo = buscarAtributo(id);
        atributo.setAtivo(false);

        return mapper.toResponse(repository.save(atributo));
    }

    public void remover(Long id) {
        repository.delete(buscarAtributo(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoAtributo buscarAtributo(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Atributo de produto não encontrado: " + id));
    }

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeAndEmpresaId(nome, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um atributo de produto com o nome informado.");
    }
}
