package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaEnderecoMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import br.com.unicos.ms_empresa.repository.EmpresaEnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação das regras de negócio relacionadas
 * aos endereços institucionais da empresa.
 *
 * <p>
 * Responsável por garantir:
 * <ul>
 *     <li>Isolamento multi-tenant</li>
 *     <li>Validação de duplicidade de endereço</li>
 *     <li>Gerenciamento de endereço principal</li>
 *     <li>Controle de permissões</li>
 * </ul>
 * </p>
 */
@Service
@Transactional
public class EmpresaEnderecoService extends BaseTenantService<EmpresaEndereco, Long> {

    private final EmpresaEnderecoRepository repository;
    private final EmpresaEnderecoMapper mapper;
    private final PermissionCheckService permissionCheckService;

    public EmpresaEnderecoService(
            EmpresaEnderecoRepository repository,
            EmpresaEnderecoMapper mapper,
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
     * Cadastra um novo endereço institucional para a empresa.
     */
    public EmpresaEnderecoResponse criar(EmpresaEnderecoCreateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_ENDERECO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar endereços da empresa.");

        Empresa empresa = empresaRef(request.empresaRefId());

        validarEnderecoDuplicado(
                empresa,
                request.logradouro(),
                request.numero(),
                request.cep()
        );

        if (request.principal())
            removerEnderecoPrincipalAtual(empresa);

        EmpresaEndereco endereco = mapper.toEntity(request);
        endereco.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(endereco));
    }

    /**
     * Atualiza um endereço institucional existente.
     */
    public EmpresaEnderecoResponse atualizar(Long id, EmpresaEnderecoUpdateRequest request) {
        if (!permissionCheckService.hasPermission("EMPRESA_ENDERECO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar endereços da empresa.");

        EmpresaEndereco endereco = buscarEndereco(id);

        boolean alterouEndereco =
                !endereco.getLogradouro().equalsIgnoreCase(request.logradouro())
                        || !endereco.getNumero().equalsIgnoreCase(request.numero())
                        || !endereco.getCep().equalsIgnoreCase(request.cep());

        if (alterouEndereco) {
            validarEnderecoDuplicado(
                    endereco.getEmpresa(),
                    request.logradouro(),
                    request.numero(),
                    request.cep()
            );
        }

        if (request.principal()) {
            removerEnderecoPrincipalAtual(endereco.getEmpresa());
            endereco.setPrincipal(true);
        } else {
            endereco.setPrincipal(false);
        }

        mapper.updateEntity(request, endereco);

        return mapper.toResponse(repository.save(endereco));
    }

    /**
     * Busca um endereço institucional pelo ID.
     */
    @Transactional(readOnly = true)
    public EmpresaEnderecoResponse buscarPorId(Long id) {
        if (!permissionCheckService.hasPermission("EMPRESA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar endereços da empresa.");
        return mapper.toResponse(buscarEndereco(id));
    }

    // ============================================================
    // LISTAGENS
    // ============================================================

    /**
     * Lista os endereços institucionais da empresa.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaEnderecoResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços da empresa.");

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
     * Lista os endereços institucionais da empresa filtrando por tipo.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaEnderecoResumoResponse> listarPorTipo(Long empresaRefId, TipoEnderecoEmpresa tipoEndereco, Pageable pageable) {
        if (!permissionCheckService.hasPermission("EMPRESA_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços da empresa.");

        Empresa empresa = empresaRef(empresaRefId);

        return repository
                .findByEmpresaAndTipoEnderecoAndEmpresaId(
                        empresa,
                        tipoEndereco,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // EXCLUSÃO
    // ============================================================

    /**
     * Remove um endereço institucional da empresa.
     */
    public void remover(Long id) {
        if (!permissionCheckService.hasPermission("EMPRESA_ENDERECO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir endereços da empresa.");
        repository.delete(buscarEndereco(id));
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private EmpresaEndereco buscarEndereco(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço institucional não encontrado: " + id));
    }

    private void validarEnderecoDuplicado(Empresa empresa, String logradouro, String numero, String cep) {
        if (repository.existsByEmpresaAndLogradouroAndNumeroAndCepAndEmpresaId(empresa, logradouro, numero, cep, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um endereço cadastrado com os dados informados para esta empresa.");
    }

    /**
     * Garante que exista apenas um endereço principal por empresa.
     */
    private void removerEnderecoPrincipalAtual(Empresa empresa) {
        repository
                .findByEmpresaAndPrincipalTrueAndEmpresaId(
                        empresa,
                        TenantContext.getEmpresaId()
                )
                .ifPresent(endereco -> {
                    endereco.setPrincipal(false);
                    repository.save(endereco);
                });
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }
}
