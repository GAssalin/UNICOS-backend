package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import br.com.unicos.ms_auth.repository.AuditoriaAcessoRepository;
import br.com.unicos.ms_auth.service.interfaces.AuditoriaAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à auditoria de acesso do sistema.
 */
@Service
@RequiredArgsConstructor
public class AuditoriaAcessoServiceImpl implements AuditoriaAcessoService {

    private final AuditoriaAcessoRepository auditoriaAcessoRepository;

    /**
     * Registra um novo evento de auditoria no banco.
     */
    @Override
    public void registrarEvento(String username, TipoAcaoAcesso acao, String detalhes, String ip) {
        AuditoriaAcesso evento = AuditoriaAcesso.builder()
                .username(username)
                .acao(acao)
                .detalhes(detalhes)
                .ip(ip)
                .build();

        auditoriaAcessoRepository.save(evento);
    }

    /**
     * Lista todos os eventos associados a um usuário específico.
     */
    @Override
    public List<AuditoriaAcessoResponse> listarPorUsuario(String username) {
        return auditoriaAcessoRepository.findByUsername(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lista todos os eventos de um tipo de ação específico.
     */
    @Override
    public List<AuditoriaAcessoResponse> listarPorAcao(TipoAcaoAcesso acao) {
        return auditoriaAcessoRepository.findByAcao(acao)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lista os eventos dentro de um intervalo de tempo.
     */
    @Override
    public List<AuditoriaAcessoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return auditoriaAcessoRepository.findByDataEventoBetween(inicio, fim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte a entidade AuditoriaAcesso para o DTO AuditoriaAcessoResponse.
     */
    private AuditoriaAcessoResponse toResponse(AuditoriaAcesso entity) {
        return new AuditoriaAcessoResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getAcao(),
                entity.getDetalhes(),
                entity.getDataEvento(),
                entity.getIp()
        );
    }
}
