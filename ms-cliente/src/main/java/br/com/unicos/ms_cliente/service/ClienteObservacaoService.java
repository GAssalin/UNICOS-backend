package br.com.unicos.ms_cliente.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
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
    private final CarteiraService carteiraService;

    public ClienteObservacaoService(ClienteObservacaoRepository repository, ClienteRepository clienteRepository, ClienteObservacaoMapper mapper, CarteiraService carteiraService) {
        super(repository);
        this.clienteRepository = clienteRepository;
        this.mapper = mapper;
        this.carteiraService = carteiraService;
    }

    public ClienteObservacaoResponse salvar(ClienteObservacaoRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        // Vendedores registram observações apenas nos clientes da própria carteira.
        boolean clienteAcessivel = carteiraService.isRestritaAoUsuarioAtual()
                ? clienteRepository.existsByIdAndEmpresaIdAndVendedorId(request.clienteId(), empresaId, UserContext.getUsuarioId())
                : clienteRepository.existsByIdAndEmpresaId(request.clienteId(), empresaId);

        if (!clienteAcessivel)
            throw new EntityNotFoundException("Cliente não encontrado: " + request.clienteId());

        ClienteObservacao entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(save(entity));
    }
}
