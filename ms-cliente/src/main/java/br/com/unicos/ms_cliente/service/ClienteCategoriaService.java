package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_cliente.dto.cliente_categoria.ClienteCategoriaRequest;
import br.com.unicos.ms_cliente.dto.cliente_categoria.ClienteCategoriaResponse;
import br.com.unicos.ms_cliente.mapper.ClienteCategoriaMapper;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import br.com.unicos.ms_cliente.repository.ClienteCategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteCategoriaService extends BaseTenantService<ClienteCategoria, Long> {

    private static final String MSG_DUPLICADO = "Categoria já existente.";

    private final ClienteCategoriaRepository repository;
    private final ClienteCategoriaMapper mapper;

    public ClienteCategoriaService(ClienteCategoriaRepository repository, ClienteCategoriaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public ClienteCategoriaResponse salvar(ClienteCategoriaRequest request) {
        if (repository.existsByNomeAndEmpresaId(request.nome(), TenantContext.getEmpresaId()))
            throw new IllegalArgumentException(MSG_DUPLICADO);

        ClienteCategoria entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(save(entity));
    }

}