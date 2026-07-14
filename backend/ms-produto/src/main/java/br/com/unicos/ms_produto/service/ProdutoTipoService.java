package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoCreateRequest;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResumoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoTipoMapper;
import br.com.unicos.ms_produto.model.ProdutoTipo;
import br.com.unicos.ms_produto.repository.ProdutoTipoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoTipoService extends BaseTenantService<ProdutoTipo, Long> {

    private final ProdutoTipoRepository repository;
    private final ProdutoTipoMapper mapper;

    public ProdutoTipoService(ProdutoTipoRepository repository, ProdutoTipoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoTipoResponse criar(ProdutoTipoCreateRequest request) {
        validarNomeDuplicado(request.nome());

        ProdutoTipo tipo = mapper.toEntity(request, TenantContext.getEmpresaId());
        tipo.setAtivo(true);

        return mapper.toResponse(repository.save(tipo));
    }

    public ProdutoTipoResponse atualizar(Long id, ProdutoTipoUpdateRequest request) {
        ProdutoTipo tipo = buscarTipo(id);

        if (!tipo.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome());

        mapper.updateEntity(request, tipo);

        return mapper.toResponse(repository.save(tipo));
    }

    @Transactional(readOnly = true)
    public ProdutoTipoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarTipo(id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoTipoResumoResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoTipoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public ProdutoTipoResponse ativar(Long id) {
        ProdutoTipo tipo = buscarTipo(id);
        tipo.setAtivo(true);

        return mapper.toResponse(repository.save(tipo));
    }

    public ProdutoTipoResponse inativar(Long id) {
        ProdutoTipo tipo = buscarTipo(id);
        tipo.setAtivo(false);

        return mapper.toResponse(repository.save(tipo));
    }

    public void remover(Long id) {
        repository.delete(buscarTipo(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoTipo buscarTipo(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de produto não encontrado: " + id));
    }

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeAndEmpresaId(nome, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um tipo de produto com o nome informado.");
    }
}
