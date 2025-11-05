package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.HistoricoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.HistoricoAtivoRequest;
import br.com.unicos.ms_ativos.dto.HistoricoAtivoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code HistoricoAtivo}.
 * <p>
 * Define os métodos para registro, consulta e análise do histórico de eventos
 * associados a cada ativo (transferências, manutenções, alterações de status etc.).
 */
public interface HistoricoAtivoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra um novo evento no histórico de um ativo.
     *
     * @param request DTO contendo os dados do evento.
     * @return DTO representando o histórico registrado.
     */
    @Transactional
    HistoricoAtivoResponse salvar(HistoricoAtivoRequest request);

    /**
     * Atualiza as informações de um histórico existente.
     *
     * @param id      identificador do histórico.
     * @param request DTO contendo os novos dados.
     * @return DTO atualizado com as informações persistidas.
     */
    @Transactional
    HistoricoAtivoResponse atualizar(Long id, HistoricoAtivoRequest request);

    /**
     * Exclui um registro de histórico com base em seu ID.
     *
     * @param id identificador do histórico.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca um histórico específico pelo seu ID.
     *
     * @param id identificador do histórico.
     * @return DTO detalhado, se encontrado.
     */
    Optional<HistoricoAtivoResponse> buscarPorId(Long id);

    /**
     * Lista todos os eventos de histórico cadastrados.
     *
     * @return lista resumida de eventos de histórico.
     */
    List<HistoricoAtivoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna o histórico completo de eventos de um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de eventos do ativo.
     */
    List<HistoricoAtivoListDTO> buscarPorAtivo(Long ativoId);

    /**
     * Retorna os eventos registrados por um usuário específico.
     *
     * @param usuarioId identificador do usuário responsável.
     * @return lista de eventos criados pelo usuário.
     */
    List<HistoricoAtivoListDTO> buscarPorUsuarioResponsavel(Long usuarioId);

    /**
     * Retorna os eventos ocorridos dentro de um intervalo de datas.
     *
     * @param inicio data e hora inicial.
     * @param fim    data e hora final.
     * @return lista de eventos dentro do intervalo.
     */
    List<HistoricoAtivoListDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Retorna o último evento registrado de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return evento mais recente, se existir.
     */
    Optional<HistoricoAtivoResponse> buscarUltimoEventoPorAtivo(Long ativoId);

    /**
     * Retorna o histórico de eventos que envolvem alterações de status.
     *
     * @param ativoId identificador do ativo.
     * @return lista de eventos relacionados à mudança de status.
     */
    List<HistoricoAtivoListDTO> buscarEventosDeStatus(Long ativoId);

    // ===========================================================
    // 📊 RELATÓRIOS E ANÁLISE
    // ===========================================================

    /**
     * Retorna o total de eventos registrados no sistema.
     *
     * @return quantidade total de registros de histórico.
     */
    Long contarTotalEventos();

    /**
     * Retorna a quantidade de eventos registrados para um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return número de eventos registrados para o ativo.
     */
    Long contarEventosPorAtivo(Long ativoId);

    /**
     * Retorna a quantidade de eventos criados por cada usuário responsável.
     *
     * @return mapa com o ID do usuário e o total de eventos criados.
     */
    List<Object[]> contarEventosPorUsuario();

    /**
     * Retorna os eventos mais recentes registrados no sistema.
     *
     * @param limite quantidade máxima de registros a retornar.
     * @return lista dos eventos mais recentes.
     */
    List<HistoricoAtivoListDTO> buscarEventosRecentes(int limite);
}
