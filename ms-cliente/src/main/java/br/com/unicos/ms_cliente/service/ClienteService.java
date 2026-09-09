package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.core.usuario.dto.UsuarioRoleIdsResponse;
import br.com.unicos.ms_cliente.client.PermissaoService;
import br.com.unicos.ms_cliente.client.UsuarioService;
import br.com.unicos.ms_cliente.dto.cliente.ClienteRequest;
import br.com.unicos.ms_cliente.dto.cliente.ClienteResponse;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import br.com.unicos.ms_cliente.mapper.ClienteMapper;
import br.com.unicos.ms_cliente.model.Cliente;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import br.com.unicos.ms_cliente.repository.ClienteCategoriaRepository;
import br.com.unicos.ms_cliente.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService extends BaseTenantService<Cliente, Long> {

    private static final String MSG_DUPLICADO = "Já existe cliente para essa pessoa.";

    private final ClienteRepository repository;
    private final ClienteCategoriaRepository categoriaRepository;
    private final ClienteMapper mapper;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public ClienteService(ClienteRepository repository, ClienteCategoriaRepository categoriaRepository, ClienteMapper mapper, UsuarioService usuarioClient, PermissaoService permissaoService) {
        super(repository);
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
        this.usuarioService = usuarioClient;
        this.permissaoService = permissaoService;
    }

    public ClienteResponse salvar(ClienteRequest request) {
        validarClienteDuplicado(request.pessoaId());

        Cliente entity = mapper.toEntity(request);
        if (entity.getVendedorId() == null)
            entity.setVendedorId(UserContext.getUsuarioId());
        entity.setEmpresaId(TenantContext.getEmpresaId());

        if (request.categoriaId() != null)
            entity.setCategoria(buscarCategoria(request.categoriaId()));

        return mapper.toResponse(save(entity));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente entity = buscar(id);

        if (!entity.getPessoaId().equals(request.pessoaId()))
            validarClienteDuplicado(request.pessoaId());

        mapper.updateEntity(entity, request);

        if (request.categoriaId() != null)
            entity.setCategoria(buscarCategoria(request.categoriaId()));
        else
            entity.setCategoria(null);

        return mapper.toResponse(save(entity));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Long usuarioId = UserContext.getUsuarioId();

        if (isUsuarioUmVendedor()) {
            return repository
                    .findByEmpresaIdAndVendedorId(
                            empresaId,
                            usuarioId,
                            pageable
                    )
                    .map(mapper::toResponse);
        }

        return findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarPorStatus(StatusCliente status, Pageable pageable) {
        return repository
                .findByStatusAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    public void deletar(Long id) {
        Cliente entity = buscar(id);
        repository.delete(entity);
    }

    // ============================================================
    // AUX
    // ============================================================

    private Cliente buscar(Long id) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));

        if (!TenantContext.getEmpresaId().equals(entity.getEmpresaId()))
            throw new AccessDeniedException("Acesso fora do tenant.");

        return entity;
    }

    private boolean isUsuarioUmVendedor() {
        UsuarioRoleIdsResponse response = usuarioService.buscarRoleIdsDoUsuario(UserContext.getUsuarioId());
        return permissaoService.buscarNomeRoleById(response.idRole()).nome().toUpperCase().contains("VENDEDOR");
    }

    private ClienteCategoria buscarCategoria(Long id) {
        ClienteCategoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        if (!TenantContext.getEmpresaId().equals(categoria.getEmpresaId()))
            throw new AccessDeniedException("Categoria fora do tenant.");

        return categoria;
    }

    private void validarClienteDuplicado(Long pessoaId) {
        if (repository.existsByPessoaIdAndEmpresaId(pessoaId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException(MSG_DUPLICADO);
    }

}