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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarcaProdutoService extends BaseTenantService<MarcaProduto, Long> {

    private final MarcaProdutoRepository repository;
    private final MarcaProdutoMapper mapper;

    public MarcaProdutoService(MarcaProdutoRepository repository, MarcaProdutoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public MarcaProdutoResponse criar(MarcaProdutoCreateRequest request) {
        validarNomeDuplicado(request.nome());

        MarcaProduto marca = mapper.toEntity(request, TenantContext.getEmpresaId());
        marca.setAtivo(true);

        return mapper.toResponse(repository.save(marca));
    }

    public MarcaProdutoResponse atualizar(Long id, MarcaProdutoUpdateRequest request) {
        MarcaProduto marca = buscarMarca(id);

        if (!marca.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome());

        mapper.updateEntity(request, marca);

        return mapper.toResponse(repository.save(marca));
    }

    @Transactional(readOnly = true)
    public MarcaProdutoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarMarca(id));
    }

    @Transactional(readOnly = true)
    public Page<MarcaProdutoResumoResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<MarcaProdutoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public MarcaProdutoResponse ativar(Long id) {
        MarcaProduto marca = buscarMarca(id);
        marca.setAtivo(true);

        return mapper.toResponse(repository.save(marca));
    }

    public MarcaProdutoResponse inativar(Long id) {
        MarcaProduto marca = buscarMarca(id);
        marca.setAtivo(false);

        return mapper.toResponse(repository.save(marca));
    }

    public void remover(Long id) {
        repository.delete(buscarMarca(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private MarcaProduto buscarMarca(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca de produto não encontrada: " + id));
    }

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeAndEmpresaId(nome, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma marca de produto com o nome informado.");
    }
}
