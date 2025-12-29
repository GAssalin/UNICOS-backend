package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaContatoMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import br.com.unicos.ms_empresa.repository.EmpresaContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação das regras de negócio relacionadas
 * aos contatos institucionais da empresa.
 *
 * <p>
 * Responsável por garantir:
 * <ul>
 *     <li>Isolamento multi-tenant</li>
 *     <li>Validação de duplicidade</li>
 *     <li>Gerenciamento de contato principal</li>
 *     <li>Controle de permissões</li>
 * </ul>
 * </p>
 */
@Service
@Transactional
public class EmpresaContatoService extends BaseTenantService<EmpresaContato, Long> {

    private final EmpresaContatoRepository repository;
    private final EmpresaContatoMapper mapper;
    private final PermissionCheckService permissionCheckService;

    public EmpresaContatoService(
            EmpresaContatoRepository repository,
            EmpresaContatoMapper mapper,
            PermissionCheckService permissionCheckService
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
        this.permissionCheckService = permissionCheckService;
    }

    // ============================================================
    // CRUD
    // ============================================================

    /**
     * Cadastra um novo contato institucional para a empresa.
     */
    public EmpresaContatoResponse criar(EmpresaContatoCreateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONTATO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar contatos da empresa.");

        Empresa empresa = empresaRef(request.empresaRefId());

        validarContatoDuplicado(empresa, request.valor());

        if (request.principal())
            removerContatoPrincipalAtual(empresa);

        EmpresaContato contato = mapper.toEntity(request);
        contato.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(contato));
    }

    /**
     * Atualiza um contato institucional existente.
     */
    public EmpresaContatoResponse atualizar(Long id, EmpresaContatoUpdateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONTATO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar contatos da empresa.");

        EmpresaContato contato = buscarContato(id);

        if (!contato.getValor().equalsIgnoreCase(request.valor())) {
            validarContatoDuplicado(contato.getEmpresa(), request.valor());
            contato.setValor(request.valor());
        }

        if (request.principal()) {
            removerContatoPrincipalAtual(contato.getEmpresa());
            contato.setPrincipal(true);
        } else {
            contato.setPrincipal(false);
        }

        return mapper.toResponse(repository.save(contato));
    }

    /**
     * Busca um contato institucional pelo ID.
     */
    @Transactional(readOnly = true)
    public EmpresaContatoResponse buscarPorId(Long id) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONTATO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar contatos da empresa.");
        return mapper.toResponse(buscarContato(id));
    }

    // ============================================================
    // LISTAGENS
    // ============================================================

    /**
     * Lista os contatos institucionais da empresa.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaContatoResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONTATO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar contatos da empresa.");

        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    /**
     * Lista os contatos institucionais da empresa filtrando por tipo.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaContatoResumoResponse> listarPorTipo(Long empresaRefId, TipoContatoEmpresa tipo, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONTATO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar contatos da empresa.");

        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndTipoContatoAndEmpresaId(
                        empresa,
                        tipo,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // EXCLUSÃO
    // ============================================================

    /**
     * Remove um contato institucional da empresa.
     */
    public void remover(Long id) {
        if (!permissionCheckService.hasPermission("EMPRESA_CONTATO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir contatos da empresa.");
        repository.delete(buscarContato(id));
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private EmpresaContato buscarContato(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato institucional não encontrado: " + id));
    }

    private void validarContatoDuplicado(Empresa empresa, String valor) {
        if (repository.existsByEmpresaAndValorAndEmpresaId(empresa, valor, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um contato institucional com o valor informado para esta empresa.");
    }

    /**
     * Garante que exista apenas um contato principal por empresa.
     */
    private void removerContatoPrincipalAtual(Empresa empresa) {
        repository
                .findByEmpresaAndPrincipalTrueAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId()
                )
                .ifPresent(contato -> {
                    contato.setPrincipal(false);
                    repository.save(contato);
                });
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}
