package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.model.HistoricoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link HistoricoAtivo}.
 * <p>
 * Permite rastrear eventos ocorridos durante o ciclo de vida dos ativos, como
 * transferências, manutenções, alterações de status e outras ocorrências relevantes.
 */
@Repository
public interface HistoricoAtivoRepository extends JpaRepository<HistoricoAtivo, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Retorna todos os registros de histórico vinculados a um ativo.
     *
     * @param ativoId identificador do ativo
     * @return lista de eventos associados ao ativo
     */
    List<HistoricoAtivo> findByAtivoId(Long ativoId);

    /**
     * Retorna os registros de histórico de um ativo, ordenados por data decrescente.
     *
     * @param ativoId identificador do ativo
     * @return lista de eventos ordenados do mais recente para o mais antigo
     */
    List<HistoricoAtivo> findByAtivoIdOrderByDataEventoDesc(Long ativoId);

    /**
     * Busca o último evento registrado para um ativo.
     *
     * @param ativoId identificador do ativo
     * @return o evento mais recente, se existir
     */
    Optional<HistoricoAtivo> findTopByAtivoIdOrderByDataEventoDesc(Long ativoId);

    /**
     * Retorna todos os eventos registrados por um determinado usuário responsável.
     *
     * @param usuarioResponsavelId identificador do usuário
     * @return lista de eventos realizados pelo usuário
     */
    List<HistoricoAtivo> findByUsuarioResponsavelId(Long usuarioResponsavelId);

    // ===========================================================
    // 📅 CONSULTAS POR PERÍODO
    // ===========================================================

    /**
     * Retorna os eventos registrados dentro de um intervalo de tempo.
     *
     * @param inicio data e hora inicial
     * @param fim    data e hora final
     * @return lista de eventos no intervalo informado
     */
    List<HistoricoAtivo> findByDataEventoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Retorna os eventos registrados em uma data específica (ignorando horário).
     *
     * @param dataInicio data inicial (00:00)
     * @param dataFim    data final (23:59)
     * @return lista de eventos ocorridos no dia
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE h.dataEvento BETWEEN :dataInicio AND :dataFim")
    List<HistoricoAtivo> buscarEventosDoDia(LocalDateTime dataInicio, LocalDateTime dataFim);

    // ===========================================================
    // 🧠 CONSULTAS CUSTOMIZADAS (JPQL)
    // ===========================================================

    /**
     * Consulta personalizada: busca eventos que contenham um determinado texto na descrição.
     *
     * @param texto parte da descrição a ser buscada
     * @return lista de eventos contendo o texto informado
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE LOWER(h.descricaoEvento) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<HistoricoAtivo> buscarPorDescricao(String texto);

    /**
     * Consulta personalizada: busca eventos associados a transferências de ativo.
     *
     * @return lista de eventos relacionados a transferências
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE LOWER(h.descricaoEvento) LIKE '%transfer%'")
    List<HistoricoAtivo> buscarEventosDeTransferencia();

    /**
     * Consulta personalizada: busca eventos associados a manutenções.
     *
     * @return lista de eventos relacionados a manutenções
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE LOWER(h.descricaoEvento) LIKE '%manuten%'")
    List<HistoricoAtivo> buscarEventosDeManutencao();

    /**
     * Consulta personalizada: busca eventos que envolvam alteração de status.
     *
     * @return lista de eventos relacionados à mudança de status
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE LOWER(h.descricaoEvento) LIKE '%status%'")
    List<HistoricoAtivo> buscarEventosDeAlteracaoDeStatus();

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna a contagem de eventos registrados por ativo.
     *
     * @return lista contendo o ID do ativo e o número de eventos registrados
     */
    @Query("SELECT h.ativo.id, COUNT(h) FROM HistoricoAtivo h GROUP BY h.ativo.id")
    List<Object[]> contarEventosPorAtivo();

    /**
     * Retorna o total de eventos registrados por usuário.
     *
     * @return lista contendo o ID do usuário e a contagem de eventos realizados
     */
    @Query("SELECT h.usuarioResponsavelId, COUNT(h) FROM HistoricoAtivo h GROUP BY h.usuarioResponsavelId")
    List<Object[]> contarEventosPorUsuario();

    /**
     * Consulta personalizada: retorna a quantidade de eventos registrados por mês e ano.
     *
     * @return lista contendo o mês/ano e a contagem total de eventos
     */
    @Query("SELECT FUNCTION('MONTH', h.dataEvento), FUNCTION('YEAR', h.dataEvento), COUNT(h) " +
            "FROM HistoricoAtivo h GROUP BY FUNCTION('MONTH', h.dataEvento), FUNCTION('YEAR', h.dataEvento)")
    List<Object[]> contarEventosPorMes();

    /**
     * Consulta personalizada: busca os ativos com maior número de eventos registrados.
     *
     * @return lista contendo o ID do ativo e a quantidade de eventos
     */
    @Query("SELECT h.ativo.id, COUNT(h) FROM HistoricoAtivo h GROUP BY h.ativo.id ORDER BY COUNT(h) DESC")
    List<Object[]> buscarAtivosMaisRegistrados();

    /**
     * Retorna eventos recentes (últimos 7 dias) para auditoria e acompanhamento.
     *
     * @return lista de eventos recentes
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE h.dataEvento >= CURRENT_TIMESTAMP - 7 ORDER BY h.dataEvento DESC")
    List<HistoricoAtivo> buscarEventosRecentes();
}
