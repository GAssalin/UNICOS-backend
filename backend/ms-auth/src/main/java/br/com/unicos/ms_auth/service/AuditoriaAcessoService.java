package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade AuditoriaAcesso.
 */
public interface AuditoriaAcessoService {

    /**
     * Registra um novo evento de auditoria no sistema.
     *
     * @param username Nome do usuário (ou null em caso de falha antes da autenticação).
     * @param acao     Tipo da ação registrada.
     * @param detalhes Detalhes adicionais do evento.
     * @param ip       Endereço IP do cliente que gerou o evento.
     */
    void registrarEvento(String username, TipoAcaoAcesso acao, String detalhes, String ip);

    /**
     * Lista todos os registros de auditoria referentes a um usuário específico.
     *
     * @param username Nome do usuário.
     * @return Lista de eventos de auditoria relacionados ao usuário.
     */
    List<AuditoriaAcessoResponse> listarPorUsuario(String username);

    /**
     * Lista todos os registros de auditoria de um determinado tipo de ação.
     *
     * @param acao Tipo da ação (LOGIN_SUCESSO, LOGIN_FALHA, LOGOUT).
     * @return Lista de eventos de auditoria do tipo informado.
     */
    List<AuditoriaAcessoResponse> listarPorAcao(TipoAcaoAcesso acao);

    /**
     * Lista registros de auditoria que ocorreram dentro de um intervalo de datas.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de eventos dentro do período informado.
     */
    List<AuditoriaAcessoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
