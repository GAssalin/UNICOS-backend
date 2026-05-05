package br.com.unicos.ms_cliente.service;

import br.com.unicos.ms_cliente.model.ClienteHistoricoStatus;
import br.com.unicos.ms_cliente.repository.ClienteHistoricoStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteHistoricoStatusService {

    private final ClienteHistoricoStatusRepository repository;

    public ClienteHistoricoStatusService(ClienteHistoricoStatusRepository repository) {
        this.repository = repository;
    }

    public void registrar(ClienteHistoricoStatus historico) {
        repository.save(historico);
    }
}