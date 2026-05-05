package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_cliente.dto.ClienteRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteResponseDTO;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import br.com.unicos.ms_cliente.mapper.ClienteMapper;
import br.com.unicos.ms_cliente.model.Cliente;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import br.com.unicos.ms_cliente.repository.ClienteCategoriaRepository;
import br.com.unicos.ms_cliente.repository.ClienteRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ClienteService extends BaseTenantService<Cliente, Long> {

    private static final String CB = "cliente-admin";
    private static final String MSG_DUPLICADO = "Já existe cliente para essa pessoa.";
    private static final String MSG_FALLBACK = "Serviço de clientes indisponível.";

    private final ClienteRepository repository;
    private final ClienteCategoriaRepository categoriaRepository;
    private final ClienteMapper mapper;

    public ClienteService(
            ClienteRepository repository,
            ClienteCategoriaRepository categoriaRepository,
            ClienteMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @CircuitBreaker(name = CB, fallbackMethod = "fallback")
    public ClienteResponseDTO salvar(ClienteRequestDTO request) {

        validarClienteDuplicado(request.getPessoaId());

        Cliente entity = mapper.toEntity(request);
        entity.setEmpresaId(obterEmpresaId());

        if (request.getCategoriaId() != null) {
            entity.setCategoria(buscarCategoria(request.getCategoriaId()));
        }

        return mapper.toResponse(save(entity));
    }

    @CircuitBreaker(name = CB, fallbackMethod = "fallbackIdReq")
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO request) {

        Cliente entity = buscar(id);

        if (!entity.getPessoaId().equals(request.getPessoaId())) {
            validarClienteDuplicado(request.getPessoaId());
        }

        mapper.updateEntity(request, entity);

        if (request.getCategoriaId() != null) {
            entity.setCategoria(buscarCategoria(request.getCategoriaId()));
        } else {
            entity.setCategoria(null);
        }

        return mapper.toResponse(save(entity));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CB, fallbackMethod = "fallbackId")
    public ClienteResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CB, fallbackMethod = "fallbackPage")
    public Page<ClienteResponseDTO> listar(Pageable pageable) {
        return findAllByEmpresaId(obterEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarPorStatus(StatusCliente status, Pageable pageable) {
        return repository
                .findByStatusAndEmpresaId(status, obterEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    @CircuitBreaker(name = CB, fallbackMethod = "fallbackVoid")
    public void deletar(Long id) {
        Cliente entity = buscar(id);
        repository.delete(entity);
    }

    private Cliente buscar(Long id) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));

        if (!obterEmpresaId().equals(entity.getEmpresaId())) {
            throw new AccessDeniedException("Acesso fora do tenant.");
        }

        return entity;
    }

    private ClienteCategoria buscarCategoria(Long id) {
        ClienteCategoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        if (!obterEmpresaId().equals(categoria.getEmpresaId())) {
            throw new AccessDeniedException("Categoria fora do tenant.");
        }

        return categoria;
    }

    private void validarClienteDuplicado(Long pessoaId) {
        if (repository.existsByPessoaIdAndEmpresaId(pessoaId, obterEmpresaId())) {
            throw new IllegalArgumentException(MSG_DUPLICADO);
        }
    }

    private Long obterEmpresaId() {
        return TenantContext.getEmpresaId();
    }

    private ClienteResponseDTO fallback(Object req, Throwable ex) {
        throw indisponivel(ex);
    }

    private ClienteResponseDTO fallbackId(Long id, Throwable ex) {
        throw indisponivel(ex);
    }

    private ClienteResponseDTO fallbackIdReq(Long id, Object req, Throwable ex) {
        throw indisponivel(ex);
    }

    private Page<ClienteResponseDTO> fallbackPage(Pageable pageable, Throwable ex) {
        throw indisponivel(ex);
    }

    private void fallbackVoid(Long id, Throwable ex) {
        throw indisponivel(ex);
    }

    private ResponseStatusException indisponivel(Throwable ex) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG_FALLBACK, ex);
    }
}