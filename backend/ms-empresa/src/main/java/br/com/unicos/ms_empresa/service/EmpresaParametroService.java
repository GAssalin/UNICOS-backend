package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.mapper.EmpresaParametroMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaParametro;
import br.com.unicos.ms_empresa.repository.EmpresaParametroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação das regras de negócio relacionadas
 * aos parâmetros flexíveis da empresa.
 *
 * <p>
 * Responsável por garantir:
 * <ul>
 *     <li>Isolamento multi-tenant</li>
 *     <li>Validação de duplicidade de chave</li>
 *     <li>Controle de permissões</li>
 * </ul>
 * </p>
 */
@Service
@Transactional
public class EmpresaParametroService extends BaseTenantService<EmpresaParametro, Long> {

    private final EmpresaParametroRepository repository;
    private final EmpresaParametroMapper mapper;
    private final PermissionCheckService permissionCheckService;

    public EmpresaParametroService(
            EmpresaParametroRepository repository,
            EmpresaParametroMapper mapper,
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
     * Cria um novo parâmetro para a empresa.
     */
    public EmpresaParametroResponse criar(EmpresaParametroCreateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_PARAMETRO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar parâmetros da empresa.");

        Empresa empresa = empresaRef(request.empresaRefId());

        validarChaveDuplicada(empresa, request.chave());

        EmpresaParametro parametro = mapper.toEntity(request);
        parametro.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(parametro));
    }

    /**
     * Atualiza o valor de um parâmetro existente.
     */
    public EmpresaParametroResponse atualizar(Long empresaRefId, String chave, EmpresaParametroUpdateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_PARAMETRO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar parâmetros da empresa.");

        EmpresaParametro parametro =
                buscarParametroPorChaveEntidade(empresaRefId, chave);

        mapper.updateEntity(request, parametro);

        return mapper.toResponse(repository.save(parametro));
    }

    /**
     * Busca um parâmetro da empresa pelo nome da chave.
     */
    @Transactional(readOnly = true)
    public EmpresaParametroResponse buscarPorChave(Long empresaRefId, String chave) {
        if (!permissionCheckService.hasPermission("EMPRESA_PARAMETRO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar parâmetros da empresa.");

        return mapper.toResponse(buscarParametroPorChaveEntidade(empresaRefId, chave));
    }

    // ============================================================
    // LISTAGENS
    // ============================================================

    /**
     * Lista os parâmetros da empresa de forma paginada.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaParametroResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_PARAMETRO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar parâmetros da empresa.");

        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // EXCLUSÃO
    // ============================================================

    /**
     * Remove um parâmetro da empresa.
     */
    public void remover(Long empresaRefId, String chave) {
        if (!permissionCheckService.hasPermission("EMPRESA_PARAMETRO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir parâmetros da empresa.");

        EmpresaParametro parametro =
                buscarParametroPorChaveEntidade(empresaRefId, chave);

        repository.delete(parametro);
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private EmpresaParametro buscarParametroPorChaveEntidade(Long empresaRefId, String chave) {
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndChaveAndEmpresaId(
                        empresa,
                        chave,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado para a chave: " + chave));
    }

    private void validarChaveDuplicada(Empresa empresa, String chave) {
        if (repository.existsByEmpresaAndChaveAndEmpresaId(empresa, chave, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um parâmetro cadastrado com a chave informada para esta empresa.");
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}
