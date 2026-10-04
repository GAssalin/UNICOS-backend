package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_cliente.dto.cliente_observacao.ClienteObservacaoRequest;
import br.com.unicos.ms_cliente.dto.cliente_observacao.ClienteObservacaoResponse;
import br.com.unicos.ms_cliente.mapper.ClienteObservacaoMapper;
import br.com.unicos.ms_cliente.model.ClienteObservacao;
import br.com.unicos.ms_cliente.repository.ClienteObservacaoRepository;
import br.com.unicos.ms_cliente.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteObservacaoService extends BaseTenantService<ClienteObservacao, Long> {

    private final ClienteRepository clienteRepository;
    private final ClienteObservacaoMapper mapper;

    public ClienteObservacaoService(ClienteObservacaoRepository repository, ClienteRepository clienteRepository, ClienteObservacaoMapper mapper) {
        super(repository);
        this.clienteRepository = clienteRepository;
        this.mapper = mapper;
    }

    public ClienteObservacaoResponse salvar(ClienteObservacaoRequest request) {
        if (!clienteRepository.existsByIdAndEmpresaId(request.clienteId(), TenantContext.getEmpresaId()))
            throw new EntityNotFoundException("Cliente não encontrado: " + request.clienteId());

        ClienteObservacao entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return mapper.toResponse(save(entity));
    }
}
