package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
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
    private final CarteiraService carteiraService;

    public ClienteService(ClienteRepository repository, ClienteCategoriaRepository categoriaRepository, ClienteMapper mapper, UsuarioService usuarioService, CarteiraService carteiraService) {
        super(repository);
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
        this.usuarioService = usuarioService;
        this.carteiraService = carteiraService;
    }

    public ClienteResponse salvar(ClienteRequest request) {
        validarClienteDuplicado(request.pessoaId());

        Cliente entity = mapper.toEntity(request);
        // Vendedores só cadastram clientes na própria carteira.
        if (entity.getVendedorId() == null || carteiraService.isRestritaAoUsuarioAtual())
            entity.setVendedorId(UserContext.getUsuarioId());
        else
            carteiraService.validarVendedor(entity.getVendedorId());
        entity.setEmpresaId(TenantContext.getEmpresaId());

        if (request.categoriaId() != null)
            entity.setCategoria(buscarCategoria(request.categoriaId()));

        return mapper.toResponse(save(entity));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        boolean carteiraRestrita = carteiraService.isRestritaAoUsuarioAtual();
        Cliente entity = buscar(id, carteiraRestrita);
        Long vendedorAnterior = entity.getVendedorId();

        if (!entity.getPessoaId().equals(request.pessoaId()))
            validarClienteDuplicado(request.pessoaId());

        mapper.updateEntity(entity, request);

        // Vendedores não transferem seus clientes para outros vendedores.
        if (carteiraRestrita)
            entity.setVendedorId(UserContext.getUsuarioId());
        else if (!entity.getVendedorId().equals(vendedorAnterior))
            carteiraService.validarVendedor(entity.getVendedorId());

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

    /**
     * @param vendedorId filtro opcional pela carteira de um vendedor; ignorado para vendedores,
     *                   que sempre recebem apenas a própria carteira
     */
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listar(Long vendedorId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Long vendedor = vendedorDaConsulta(vendedorId);

        Page<Cliente> clientes = vendedor != null
                ? repository.findByEmpresaIdAndVendedorId(empresaId, vendedor, pageable)
                : findAllByEmpresaId(empresaId, pageable);

        return comNomesDosVendedores(clientes);
    }

    /**
     * @param vendedorId filtro opcional pela carteira de um vendedor; ignorado para vendedores,
     *                   que sempre recebem apenas a própria carteira
     */
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarPorStatus(StatusCliente status, Long vendedorId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Long vendedor = vendedorDaConsulta(vendedorId);

        Page<Cliente> clientes = vendedor != null
                ? repository.findByStatusAndEmpresaIdAndVendedorId(status, empresaId, vendedor, pageable)
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
        return buscar(id, carteiraService.isRestritaAoUsuarioAtual());
    }

    /**
     * Vendedores acessam apenas os clientes da própria carteira; os demais respondem como inexistentes.
     */
    private Cliente buscar(Long id, boolean carteiraRestrita) {
        return findById(id)
                .filter(cliente -> !carteiraRestrita || UserContext.getUsuarioId().equals(cliente.getVendedorId()))
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
    }

    /**
     * Vendedor cuja carteira será consultada: o próprio usuário quando a carteira é restrita; caso
     * contrário, o filtro informado ({@code null} para todos os clientes).
     */
    private Long vendedorDaConsulta(Long vendedorId) {
        return carteiraService.isRestritaAoUsuarioAtual() ? UserContext.getUsuarioId() : vendedorId;
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

    private ClienteCategoria buscarCategoria(Long id) {
        return categoriaRepository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));
    }

    private void validarClienteDuplicado(Long pessoaId) {
        if (repository.existsByPessoaIdAndEmpresaId(pessoaId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException(MSG_DUPLICADO);
    }

}