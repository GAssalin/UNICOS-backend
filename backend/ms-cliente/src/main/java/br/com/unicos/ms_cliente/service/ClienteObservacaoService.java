package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_cliente.dto.ClienteObservacaoRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteObservacaoResponseDTO;
import br.com.unicos.ms_cliente.mapper.ClienteObservacaoMapper;
import br.com.unicos.ms_cliente.model.ClienteObservacao;
import br.com.unicos.ms_cliente.repository.ClienteObservacaoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteObservacaoService extends BaseTenantService<ClienteObservacao, Long> {

    private static final String CB = "cliente-obs";

    private final ClienteObservacaoRepository repository;
    private final ClienteObservacaoMapper mapper;

    public ClienteObservacaoService(
            ClienteObservacaoRepository repository,
            ClienteObservacaoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @CircuitBreaker(name = CB)
    public ClienteObservacaoResponseDTO salvar(ClienteObservacaoRequestDTO request) {

        ClienteObservacao entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(save(entity));
    }
}