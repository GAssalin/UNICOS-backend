package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_cliente.dto.ClienteCategoriaRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteCategoriaResponseDTO;
import br.com.unicos.ms_cliente.mapper.ClienteCategoriaMapper;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import br.com.unicos.ms_cliente.repository.ClienteCategoriaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ClienteCategoriaService extends BaseTenantService<ClienteCategoria, Long> {

    private static final String CB = "cliente-categoria";
    private static final String MSG_DUPLICADO = "Categoria já existente.";
    private static final String MSG_FALLBACK = "Serviço indisponível.";

    private final ClienteCategoriaRepository repository;
    private final ClienteCategoriaMapper mapper;

    public ClienteCategoriaService(
            ClienteCategoriaRepository repository,
            ClienteCategoriaMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @CircuitBreaker(name = CB, fallbackMethod = "fallback")
    public ClienteCategoriaResponseDTO salvar(ClienteCategoriaRequestDTO request) {

        if (repository.existsByNomeAndEmpresaId(request.getNome(), obterEmpresaId())) {
            throw new IllegalArgumentException(MSG_DUPLICADO);
        }

        ClienteCategoria entity = mapper.toEntity(request);
        entity.setEmpresaId(obterEmpresaId());

        return mapper.toResponse(save(entity));
    }

    private Long obterEmpresaId() {
        return TenantContext.getEmpresaId();
    }

    private ClienteCategoriaResponseDTO fallback(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MSG_FALLBACK, ex);
    }
}