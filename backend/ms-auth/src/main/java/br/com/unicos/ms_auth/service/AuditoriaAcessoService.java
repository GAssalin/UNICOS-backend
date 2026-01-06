package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.mapper.AuditoriaAcessoMapper;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import br.com.unicos.ms_auth.repository.AuditoriaAcessoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuditoriaAcessoService extends BaseTenantService<AuditoriaAcesso, Long> {

    private final AuditoriaAcessoRepository auditoriaAcessoRepository;
    private final AuditoriaAcessoMapper auditoriaAcessoMapper;

    public AuditoriaAcessoService(
            AuditoriaAcessoRepository auditoriaAcessoRepository,
            AuditoriaAcessoMapper auditoriaAcessoMapper
    ) {
        super(auditoriaAcessoRepository);
        this.auditoriaAcessoRepository = auditoriaAcessoRepository;
        this.auditoriaAcessoMapper = auditoriaAcessoMapper;
    }

    // ============================================================
    // REGISTRO (SEM CIRCUIT BREAKER)
    // ============================================================

    public void registrarEvento(
            String username,
            TipoAcaoAcesso acao,
            String detalhes,
            String ip
    ) {
        AuditoriaAcesso evento = AuditoriaAcesso.builder()
                .username(username)
                .acao(acao)
                .detalhes(detalhes)
                .ip(ip)
                .empresaId(TenantContext.getEmpresaId())
                .dataEvento(LocalDateTime.now())
                .build();

        auditoriaAcessoRepository.save(evento);
    }

    // ============================================================
    // CONSULTAS (COM CIRCUIT BREAKER)
    // ============================================================

    @CircuitBreaker(name = "auth-auditoria-admin", fallbackMethod = "fallbackPage")
    public Page<AuditoriaAcessoResponse> listarPorUsuario(
            String username,
            Pageable pageable
    ) {
        return auditoriaAcessoRepository
                .findByUsernameAndEmpresaId(
                        username,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(auditoriaAcessoMapper::toResponse);
    }

    @CircuitBreaker(name = "auth-auditoria-admin", fallbackMethod = "fallbackPage")
    public Page<AuditoriaAcessoResponse> listarPorAcao(
            TipoAcaoAcesso acao,
            Pageable pageable
    ) {
        return auditoriaAcessoRepository
                .findByAcaoAndEmpresaId(
                        acao,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(auditoriaAcessoMapper::toResponse);
    }

    @CircuitBreaker(name = "auth-auditoria-admin", fallbackMethod = "fallbackPage")
    public Page<AuditoriaAcessoResponse> listarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    ) {
        return auditoriaAcessoRepository
                .findByDataEventoBetweenAndEmpresaId(
                        inicio,
                        fim,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(auditoriaAcessoMapper::toResponse);
    }

    // ============================================================
    // FALLBACK
    // ============================================================

    private Page<AuditoriaAcessoResponse> fallbackPage(
            Object param1,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de auditoria temporariamente indisponível");
    }
}
