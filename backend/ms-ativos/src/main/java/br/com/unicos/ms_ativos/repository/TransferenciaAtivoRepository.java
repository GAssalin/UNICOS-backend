package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.enums.TipoTransferencia;
import br.com.unicos.ms_ativos.model.TransferenciaAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link TransferenciaAtivo}.
 * <p>
 * Oferece consultas específicas para rastrear movimentações de ativos entre filiais,
 * unidades ou departamentos, além de relatórios consolidados de transferências.
 */
@Repository
public interface TransferenciaAtivoRepository extends JpaRepository<TransferenciaAtivo, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Retorna todas as transferências relacionadas a um determinado ativo.
     *
     * @param ativoId identificador do ativo
     * @return lista de transferências associadas ao ativo
     */
    List<TransferenciaAtivo> findByAtivoId(Long ativoId);

    /**
     * Retorna todas as transferências filtradas por tipo (interna, entre filiais, baixa, etc.).
     *
     * @param tipo tipo de transferência
     * @return lista de transferências correspondentes
     */
    List<TransferenciaAtivo> findByTipo(TipoTransferencia tipo);

    /**
     * Retorna todas as transferências originadas de uma determinada unidade.
     *
     * @param origemId identificador da unidade de origem
     * @return lista de transferências de saída
     */
    List<TransferenciaAtivo> findByOrigemId(Long origemId);

    /**
     * Retorna todas as transferências cujo destino seja uma unidade específica.
     *
     * @param destinoId identificador da unidade de destino
     * @return lista de transferências de entrada
     */
    List<TransferenciaAtivo> findByDestinoId(Long destinoId);

    /**
     * Busca a última transferência registrada para um ativo.
     *
     * @param ativoId identificador do ativo
     * @return última transferência registrada, se existir
     */
    Optional<TransferenciaAtivo> findTopByAtivoIdOrderByDataTransferenciaDesc(Long ativoId);

    // ===========================================================
    // 📅 CONSULTAS POR PERÍODO
    // ===========================================================

    /**
     * Retorna todas as transferências realizadas em um determinado período.
     *
     * @param inicio data inicial do intervalo
     * @param fim    data final do intervalo
     * @return lista de transferências no período informado
     */
    List<TransferenciaAtivo> findByDataTransferenciaBetween(LocalDate inicio, LocalDate fim);

    /**
     * Retorna as transferências realizadas em uma data específica.
     *
     * @param dataTransferencia data da transferência
     * @return lista de transferências do dia
     */
    List<TransferenciaAtivo> findByDataTransferencia(LocalDate dataTransferencia);

    // ===========================================================
    // 🧭 CONSULTAS CUSTOMIZADAS (JPQL)
    // ===========================================================

    /**
     * Consulta personalizada: busca transferências internas (origem = destino).
     *
     * @return lista de transferências internas
     */
    @Query("SELECT t FROM TransferenciaAtivo t WHERE t.origemId = t.destinoId")
    List<TransferenciaAtivo> buscarTransferenciasInternas();

    /**
     * Consulta personalizada: busca transferências entre filiais distintas.
     *
     * @return lista de transferências entre filiais
     */
    @Query("SELECT t FROM TransferenciaAtivo t WHERE t.origemId <> t.destinoId AND t.tipo = 'ENTRE_FILIAIS'")
    List<TransferenciaAtivo> buscarTransferenciasEntreFiliais();

    /**
     * Consulta personalizada: busca transferências para baixa contábil.
     *
     * @return lista de transferências marcadas como baixa
     */
    @Query("SELECT t FROM TransferenciaAtivo t WHERE t.tipo = 'BAIXA'")
    List<TransferenciaAtivo> buscarTransferenciasParaBaixa();

    /**
     * Consulta personalizada: retorna a contagem de transferências realizadas por tipo.
     *
     * @return lista contendo o tipo e o número de transferências
     */
    @Query("SELECT t.tipo, COUNT(t) FROM TransferenciaAtivo t GROUP BY t.tipo")
    List<Object[]> contarTransferenciasPorTipo();

    /**
     * Consulta personalizada: retorna a quantidade de transferências por unidade de origem.
     *
     * @return lista com o ID da origem e a contagem de transferências
     */
    @Query("SELECT t.origemId, COUNT(t) FROM TransferenciaAtivo t GROUP BY t.origemId")
    List<Object[]> contarTransferenciasPorOrigem();

    /**
     * Consulta personalizada: retorna a quantidade de transferências por unidade de destino.
     *
     * @return lista com o ID do destino e a contagem de transferências
     */
    @Query("SELECT t.destinoId, COUNT(t) FROM TransferenciaAtivo t GROUP BY t.destinoId")
    List<Object[]> contarTransferenciasPorDestino();

    /**
     * Consulta personalizada: busca todas as transferências realizadas por um usuário responsável.
     *
     * @param responsavelId identificador do usuário responsável
     * @return lista de transferências executadas pelo responsável
     */
    @Query("SELECT t FROM TransferenciaAtivo t WHERE t.responsavelId = :responsavelId")
    List<TransferenciaAtivo> buscarTransferenciasPorResponsavel(Long responsavelId);

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna o total de transferências realizadas por mês e ano.
     *
     * @return lista de objetos contendo o mês/ano e a contagem de transferências
     */
    @Query("SELECT FUNCTION('MONTH', t.dataTransferencia), FUNCTION('YEAR', t.dataTransferencia), COUNT(t) " +
            "FROM TransferenciaAtivo t GROUP BY FUNCTION('MONTH', t.dataTransferencia), FUNCTION('YEAR', t.dataTransferencia)")
    List<Object[]> contarTransferenciasPorMes();

    /**
     * Consulta personalizada: retorna os ativos mais transferidos (com maior número de movimentações).
     *
     * @return lista contendo o ID do ativo e o número de transferências realizadas
     */
    @Query("SELECT t.ativo.id, COUNT(t) FROM TransferenciaAtivo t GROUP BY t.ativo.id ORDER BY COUNT(t) DESC")
    List<Object[]> buscarAtivosMaisTransferidos();

    /**
     * Consulta personalizada: busca transferências realizadas recentemente (últimos 30 dias).
     *
     * @return lista de transferências recentes
     */
    @Query("SELECT t FROM TransferenciaAtivo t WHERE t.dataTransferencia >= CURRENT_DATE - 30 ORDER BY t.dataTransferencia DESC")
    List<TransferenciaAtivo> buscarTransferenciasRecentes();
}
