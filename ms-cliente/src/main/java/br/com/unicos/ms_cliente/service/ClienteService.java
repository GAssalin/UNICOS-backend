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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        // Vendedores só cadastram clientes para si mesmos.
        if (entity.getVendedorId() == null || isUsuarioUmVendedor())
            entity.setVendedorId(UserContext.getUsuarioId());
        entity.setEmpresaId(TenantContext.getEmpresaId());

        if (request.categoriaId() != null)
            entity.setCategoria(buscarCategoria(request.categoriaId()));

        return mapper.toResponse(save(entity));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        boolean vendedor = isUsuarioUmVendedor();
        Cliente entity = buscar(id, vendedor);

        if (!entity.getPessoaId().equals(request.pessoaId()))
            validarClienteDuplicado(request.pessoaId());

        mapper.updateEntity(entity, request);

        // Vendedores não transferem seus clientes para outros vendedores.
        if (vendedor)
            entity.setVendedorId(UserContext.getUsuarioId());

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

        Page<Cliente> clientes = isUsuarioUmVendedor()
                ? repository.findByEmpresaIdAndVendedorId(empresaId, usuarioId, pageable)
                : findAllByEmpresaId(empresaId, pageable);

        return comNomesDosVendedores(clientes);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarPorStatus(StatusCliente status, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        Page<Cliente> clientes = isUsuarioUmVendedor()
                ? repository.findByStatusAndEmpresaIdAndVendedorId(status, empresaId, UserContext.getUsuarioId(), pageable)
                : repository.findByStatusAndEmpresaId(status, empresaId, pageable);

        return comNomesDosVendedores(clientes);
    }

    public void deletar(Long id) {
        Cliente entity = buscar(id);
        repository.delete(entity);
    }

    // ============================================================
    // AUX
    // ============================================================

    private Cliente buscar(Long id) {
        return buscar(id, isUsuarioUmVendedor());
    }

    /**
     * Vendedores acessam apenas os próprios clientes; os demais respondem como inexistentes.
     */
    private Cliente buscar(Long id, boolean vendedor) {
        return findById(id)
                .filter(cliente -> !vendedor || UserContext.getUsuarioId().equals(cliente.getVendedorId()))
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
    }

    /**
     * Resolve o nome de cada vendedor uma única vez por página, evitando uma chamada ao
     * ms-pessoas para cada cliente.
     */
    private Page<ClienteResponse> comNomesDosVendedores(Page<Cliente> clientes) {
        Map<Long, Optional<String>> nomes = new HashMap<>();

        return clientes.map(cliente -> mapper.toResponse(
                cliente,
                cliente.getVendedorId() == null ? null : nomes
                        .computeIfAbsent(cliente.getVendedorId(), id -> Optional.ofNullable(usuarioService.buscarNome(id)))
                        .orElse(null)
        ));
    }

    private boolean isUsuarioUmVendedor() {
        UsuarioRoleIdsResponse response = usuarioService.buscarRoleIdsDoUsuario(UserContext.getUsuarioId());

        if (response == null || response.idRole() == null)
            return false;

        return permissaoService.buscarNomeRoleById(response.idRole()).nome().toUpperCase().contains("VENDEDOR");
    }

    private ClienteCategoria buscarCategoria(Long id) {
        return categoriaRepository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));
    }

    private void validarClienteDuplicado(Long pessoaId) {
        if (repository.existsByPessoaIdAndEmpresaId(pessoaId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException(MSG_DUPLICADO);
    }

}