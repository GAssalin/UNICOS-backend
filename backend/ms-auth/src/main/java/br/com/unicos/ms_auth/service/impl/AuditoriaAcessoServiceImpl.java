package br.com.unicos.ms_auth.service.impl;

import br.com.unicos.ms_auth.dto.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import br.com.unicos.ms_auth.repository.AuditoriaAcessoRepository;
import br.com.unicos.ms_auth.service.AuditoriaAcessoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação da interface {@link AuditoriaAcessoService}.
 * <p>
 * Responsável pelo registro e consulta de eventos de autenticação e auditoria.
 */
@Service
@RequiredArgsConstructor
public class AuditoriaAcessoServiceImpl implements AuditoriaAcessoService {

    private final AuditoriaAcessoRepository auditoriaAcessoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void registrarEvento(String username, String acao, String detalhes, String ip) {
        AuditoriaAcesso auditoria = AuditoriaAcesso.builder()
                .username(username)
                .acao(acao)
                .detalhes(detalhes)
                .ip(ip)
                .dataEvento(LocalDateTime.now())
                .build();

        auditoriaAcessoRepository.save(auditoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaAcessoResponse> listarPorUsuario(String username) {
        return auditoriaAcessoRepository.findByUsername(username).stream()
                .map(a -> modelMapper.map(a, AuditoriaAcessoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaAcessoResponse> listarPorAcao(String acao) {
        return auditoriaAcessoRepository.findByAcao(acao).stream()
                .map(a -> modelMapper.map(a, AuditoriaAcessoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaAcessoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return auditoriaAcessoRepository.findByDataEventoBetween(inicio, fim).stream()
                .map(a -> modelMapper.map(a, AuditoriaAcessoResponse.class))
                .collect(Collectors.toList());
    }
}
