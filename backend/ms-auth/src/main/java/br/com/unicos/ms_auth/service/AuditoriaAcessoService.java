package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.mapper.AuditoriaAcessoMapper;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import br.com.unicos.ms_auth.repository.AuditoriaAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à auditoria de acesso do sistema.
 *
 * <p>
 * Todas as operações são obrigatoriamente restritas ao tenant (empresa)
 * e utilizam paginação para evitar carga excessiva de dados.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuditoriaAcessoService {

    private final AuditoriaAcessoRepository auditoriaAcessoRepository;
    private final AuditoriaAcessoMapper auditoriaAcessoMapper;

    /**
     * Registra um novo evento de auditoria no sistema.
     *
     * @param username Usuário responsável pela ação
     * @param acao     Tipo da ação executada
     * @param detalhes Detalhes adicionais da ação
     * @param ip       Endereço IP de origem
     */
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

    /**
     * Lista eventos de auditoria de um usuário específico,
     * restritos a uma empresa (tenant).
     *
     * @param username Nome do usuário
     * @param pageable Paginação e ordenação
     * @return Página de eventos de auditoria
     */
    public Page<AuditoriaAcessoResponse> listarPorUsuario(
            String username,
            Pageable pageable
    ) {
        return auditoriaAcessoRepository
                .findByUsernameAndEmpresaId(username, TenantContext.getEmpresaId(), pageable)
                .map(auditoriaAcessoMapper::toResponse);
    }

    /**
     * Lista eventos de auditoria por tipo de ação,
     * restritos a uma empresa (tenant).
     *
     * @param acao     Tipo da ação
     * @param pageable Paginação e ordenação
     * @return Página de eventos de auditoria
     */
    public Page<AuditoriaAcessoResponse> listarPorAcao(
            TipoAcaoAcesso acao,
            Pageable pageable
    ) {
        return auditoriaAcessoRepository
                .findByAcaoAndEmpresaId(acao, TenantContext.getEmpresaId(), pageable)
                .map(auditoriaAcessoMapper::toResponse);
    }

    /**
     * Lista eventos de auditoria ocorridos dentro de um período,
     * restritos a uma empresa (tenant).
     *
     * @param inicio   Data/hora inicial
     * @param fim      Data/hora final
     * @param pageable Paginação e ordenação
     * @return Página de eventos de auditoria
     */
    public Page<AuditoriaAcessoResponse> listarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    ) {
        return auditoriaAcessoRepository
                .findByDataEventoBetweenAndEmpresaId(inicio, fim, TenantContext.getEmpresaId(), pageable)
                .map(auditoriaAcessoMapper::toResponse);
    }
}
