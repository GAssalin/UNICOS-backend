package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaContatoMapper;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import br.com.unicos.ms_empresa.repository.EmpresaContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmpresaContatoService extends BaseTenantService<EmpresaContato, Long> {

    private final EmpresaContatoRepository repository;
    private final EmpresaContatoMapper mapper;

    public EmpresaContatoService(EmpresaContatoRepository repository, EmpresaContatoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmpresaContatoResponse criar(EmpresaContatoCreateRequest request) {
        validarContatoDuplicado(request.valor(), TenantContext.getEmpresaId());

        if (request.principal())
            removerContatoPrincipalAtual(TenantContext.getEmpresaId());

        EmpresaContato contato = mapper.toEntity(request);
        contato.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(contato));
    }

    public EmpresaContatoResponse atualizar(Long id, EmpresaContatoUpdateRequest request) {
        EmpresaContato contato = buscarContato(id, TenantContext.getEmpresaId());

        if (!contato.getValor().equalsIgnoreCase(request.valor()))
            validarContatoDuplicado(request.valor(), TenantContext.getEmpresaId());
        if (request.principal())
            removerContatoPrincipalAtual(TenantContext.getEmpresaId(), id);

        mapper.updateEntity(request, contato);

        return mapper.toResponse(repository.save(contato));
    }

    @Transactional(readOnly = true)
    public EmpresaContatoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarContato(id, TenantContext.getEmpresaId()));
    }

    @Transactional(readOnly = true)
    public Page<EmpresaContatoResumoResponse> listar(Pageable pageable) {
        return repository.findByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<EmpresaContatoResumoResponse> listarPorTipo(TipoContatoEmpresa tipo, Pageable pageable) {
        return repository.findByTipoContatoAndEmpresaId(tipo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public void remover(Long id) {
        repository.delete(buscarContato(id, TenantContext.getEmpresaId()));
    }

    // ============================================================
    // AUX
    // ============================================================

    private EmpresaContato buscarContato(Long id, Long empresaId) {
        return repository.findById(id)
                .filter(contato -> empresaId.equals(contato.getEmpresaId()))
                .orElseThrow(() ->
                        new EntityNotFoundException("Contato institucional não encontrado: " + id));
    }

    private void validarContatoDuplicado(String valor, Long empresaId) {
        if (repository.existsByValorAndEmpresaId(valor, empresaId))
            throw new IllegalArgumentException("Já existe um contato institucional com o valor informado para esta empresa.");
    }

    private void removerContatoPrincipalAtual(Long empresaId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    repository.save(contato);
                });
    }

    private void removerContatoPrincipalAtual(Long empresaId, Long contatoAtualId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .filter(contato -> !contato.getId().equals(contatoAtualId))
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    repository.save(contato);
                });
    }
}