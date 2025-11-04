package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.AuditoriaAcessoResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade AuditoriaAcesso.
 */
public interface AuditoriaAcessoService {

    /**
     * Registra um novo evento de auditoria.
     *
     * @param username Nome do usuário.
     * @param acao     Tipo da ação (ex: LOGIN_SUCESSO, LOGIN_FALHA).
     * @param detalhes Detalhes do evento.
     * @param ip       Endereço IP do cliente.
     */
    void registrarEvento(String username, String acao, String detalhes, String ip);

    /**
     * Lista todos os registros de auditoria de um determinado usuário.
     *
     * @param username Nome do usuário.
     * @return Lista de AuditoriaAcessoResponse.
     */
    List<AuditoriaAcessoResponse> listarPorUsuario(String username);

    /**
     * Lista registros de auditoria de um tipo específico de ação.
     *
     * @param acao Tipo da ação.
     * @return Lista de AuditoriaAcessoResponse.
     */
    List<AuditoriaAcessoResponse> listarPorAcao(String acao);

    /**
     * Lista registros de auditoria dentro de um intervalo de tempo.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de AuditoriaAcessoResponse.
     */
    List<AuditoriaAcessoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
