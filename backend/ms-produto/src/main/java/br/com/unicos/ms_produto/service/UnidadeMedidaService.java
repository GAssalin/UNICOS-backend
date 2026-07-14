package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaCreateRequest;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaResponse;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaResumoResponse;
import br.com.unicos.ms_produto.dto.unidademedida.UnidadeMedidaUpdateRequest;
import br.com.unicos.ms_produto.mapper.UnidadeMedidaMapper;
import br.com.unicos.ms_produto.model.UnidadeMedida;
import br.com.unicos.ms_produto.repository.UnidadeMedidaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UnidadeMedidaService extends BaseTenantService<UnidadeMedida, Long> {

    private final UnidadeMedidaRepository repository;
    private final UnidadeMedidaMapper mapper;

    public UnidadeMedidaService(UnidadeMedidaRepository repository, UnidadeMedidaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public UnidadeMedidaResponse criar(UnidadeMedidaCreateRequest request) {
        validarCodigoDuplicado(request.codigo());

        UnidadeMedida unidade = mapper.toEntity(request, TenantContext.getEmpresaId());
        unidade.setAtivo(true);

        return mapper.toResponse(repository.save(unidade));
    }

    public UnidadeMedidaResponse atualizar(Long id, UnidadeMedidaUpdateRequest request) {
        UnidadeMedida unidade = buscarUnidade(id);

        if (!unidade.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        mapper.updateEntity(request, unidade);

        return mapper.toResponse(repository.save(unidade));
    }

    @Transactional(readOnly = true)
    public UnidadeMedidaResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarUnidade(id));
    }

    @Transactional(readOnly = true)
    public UnidadeMedidaResponse buscarPorCodigo(String codigo) {
        UnidadeMedida unidade = repository.findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada para o código: " + codigo));

        return mapper.toResponse(unidade);
    }

    @Transactional(readOnly = true)
    public Page<UnidadeMedidaResumoResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<UnidadeMedidaResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public UnidadeMedidaResponse ativar(Long id) {
        UnidadeMedida unidade = buscarUnidade(id);
        unidade.setAtivo(true);

        return mapper.toResponse(repository.save(unidade));
    }

    public UnidadeMedidaResponse inativar(Long id) {
        UnidadeMedida unidade = buscarUnidade(id);
        unidade.setAtivo(false);

        return mapper.toResponse(repository.save(unidade));
    }

    public void remover(Long id) {
        repository.delete(buscarUnidade(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private UnidadeMedida buscarUnidade(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada: " + id));
    }

    private void validarCodigoDuplicado(String codigo) {
        if (repository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma unidade de medida com o código informado.");
    }
}
