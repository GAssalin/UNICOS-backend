package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.enums.TipoDepreciacao;
import br.com.unicos.ms_ativos.model.DepreciacaoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link DepreciacaoAtivo}.
 * <p>
 * Fornece consultas específicas para controle contábil de depreciações,
 * permitindo cálculos de saldo, relatórios de desempenho e análise histórica.
 */
@Repository
public interface DepreciacaoAtivoRepository extends JpaRepository<DepreciacaoAtivo, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Retorna todas as depreciações vinculadas a um ativo específico.
     *
     * @param ativoId identificador do ativo
     * @return lista de depreciações associadas ao ativo
     */
    List<DepreciacaoAtivo> findByAtivoId(Long ativoId);

    /**
     * Busca depreciações de um ativo para um tipo específico.
     *
     * @param ativoId identificador do ativo
     * @param tipo    tipo de depreciação
     * @return lista de depreciações que atendem aos critérios
     */
    List<DepreciacaoAtivo> findByAtivoIdAndTipo(Long ativoId, TipoDepreciacao tipo);

    /**
     * Retorna todas as depreciações realizadas em um determinado período.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de depreciações realizadas no intervalo
     */
    List<DepreciacaoAtivo> findByDataCompetenciaBetween(LocalDate inicio, LocalDate fim);

    /**
     * Busca a depreciação de um ativo em uma data de competência específica.
     *
     * @param ativoId         identificador do ativo
     * @param dataCompetencia data de competência
     * @return depreciação correspondente, se existir
     */
    Optional<DepreciacaoAtivo> findByAtivoIdAndDataCompetencia(Long ativoId, LocalDate dataCompetencia);

    // ===========================================================
    // 💰 CONSULTAS FINANCEIRAS
    // ===========================================================

    /**
     * Retorna o valor total depreciado de um ativo ao longo do tempo.
     *
     * @param ativoId identificador do ativo
     * @return soma total de valores depreciados
     */
    @Query("SELECT COALESCE(SUM(d.valorDepreciado), 0) FROM DepreciacaoAtivo d WHERE d.ativo.id = :ativoId")
    BigDecimal calcularValorTotalDepreciado(Long ativoId);

    /**
     * Retorna o saldo contábil mais recente do ativo.
     *
     * @param ativoId identificador do ativo
     * @return último saldo contábil conhecido
     */
    @Query("SELECT d.saldoContabil FROM DepreciacaoAtivo d WHERE d.ativo.id = :ativoId ORDER BY d.dataCompetencia DESC LIMIT 1")
    Optional<BigDecimal> buscarSaldoContabilMaisRecente(Long ativoId);

    /**
     * Retorna o valor médio de depreciação mensal de um ativo.
     *
     * @param ativoId identificador do ativo
     * @return valor médio depreciado por mês
     */
    @Query("SELECT AVG(d.valorDepreciado) FROM DepreciacaoAtivo d WHERE d.ativo.id = :ativoId")
    BigDecimal calcularMediaDepreciacaoMensal(Long ativoId);

    /**
     * Retorna todas as depreciações cujo valor depreciado seja superior ao valor informado.
     *
     * @param valor valor mínimo
     * @return lista de depreciações com valores acima do informado
     */
    List<DepreciacaoAtivo> findByValorDepreciadoGreaterThan(BigDecimal valor);

    // ===========================================================
    // 🧠 CONSULTAS CUSTOMIZADAS (JPQL)
    // ===========================================================

    /**
     * Consulta personalizada: busca depreciações lineares realizadas no período atual.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de depreciações lineares no período
     */
    @Query("SELECT d FROM DepreciacaoAtivo d WHERE d.tipo = 'LINEAR' AND d.dataCompetencia BETWEEN :inicio AND :fim")
    List<DepreciacaoAtivo> buscarDepreciacoesLinearesNoPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Consulta personalizada: busca depreciações aceleradas com valor superior à média geral.
     *
     * @return lista de depreciações aceleradas acima da média
     */
    @Query("SELECT d FROM DepreciacaoAtivo d WHERE d.tipo = 'ACELERADA' AND d.valorDepreciado > " +
            "(SELECT AVG(d2.valorDepreciado) FROM DepreciacaoAtivo d2 WHERE d2.tipo = 'ACELERADA')")
    List<DepreciacaoAtivo> buscarDepreciacoesAceleradasAcimaDaMedia();

    /**
     * Consulta personalizada: retorna a última depreciação registrada para um ativo.
     *
     * @param ativoId identificador do ativo
     * @return depreciação mais recente
     */
    @Query("SELECT d FROM DepreciacaoAtivo d WHERE d.ativo.id = :ativoId ORDER BY d.dataCompetencia DESC LIMIT 1")
    Optional<DepreciacaoAtivo> buscarUltimaDepreciacaoPorAtivo(Long ativoId);

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna o total de depreciações agrupadas por tipo.
     *
     * @return lista contendo o tipo e a contagem de registros
     */
    @Query("SELECT d.tipo, COUNT(d) FROM DepreciacaoAtivo d GROUP BY d.tipo")
    List<Object[]> contarDepreciacoesPorTipo();

    /**
     * Retorna o valor total depreciado agrupado por tipo.
     *
     * @return lista contendo o tipo e a soma total de valores depreciados
     */
    @Query("SELECT d.tipo, SUM(d.valorDepreciado) FROM DepreciacaoAtivo d GROUP BY d.tipo")
    List<Object[]> somarValorDepreciadoPorTipo();

    /**
     * Consulta personalizada: retorna a média do valor depreciado por mês e ano.
     *
     * @return lista contendo o mês/ano e o valor médio depreciado
     */
    @Query("SELECT FUNCTION('MONTH', d.dataCompetencia), FUNCTION('YEAR', d.dataCompetencia), AVG(d.valorDepreciado) " +
            "FROM DepreciacaoAtivo d GROUP BY FUNCTION('MONTH', d.dataCompetencia), FUNCTION('YEAR', d.dataCompetencia)")
    List<Object[]> calcularMediaDepreciacaoPorMes();

    /**
     * Retorna o valor total de depreciação por ativo (para dashboards financeiros).
     *
     * @return lista contendo o ID do ativo e o valor total depreciado
     */
    @Query("SELECT d.ativo.id, SUM(d.valorDepreciado) FROM DepreciacaoAtivo d GROUP BY d.ativo.id")
    List<Object[]> somarDepreciacaoPorAtivo();
}
