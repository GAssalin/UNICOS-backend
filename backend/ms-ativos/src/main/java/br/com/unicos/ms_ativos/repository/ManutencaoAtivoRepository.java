package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
import br.com.unicos.ms_ativos.model.ManutencaoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link ManutencaoAtivo}.
 * <p>
 * Oferece consultas abrangentes para filtragem de manutenções por ativo, fornecedor, tipo,
 * custo e período, além de agregações úteis para relatórios de desempenho e controle de gastos.
 */
@Repository
public interface ManutencaoAtivoRepository extends JpaRepository<ManutencaoAtivo, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Retorna todas as manutenções associadas a um determinado ativo.
     *
     * @param ativoId identificador do ativo
     * @return lista de manutenções do ativo
     */
    List<ManutencaoAtivo> findByAtivoId(Long ativoId);

    /**
     * Busca todas as manutenções realizadas por um fornecedor específico.
     *
     * @param fornecedorId identificador do fornecedor
     * @return lista de manutenções executadas pelo fornecedor
     */
    List<ManutencaoAtivo> findByFornecedorId(Long fornecedorId);

    /**
     * Retorna todas as manutenções filtradas por tipo (preventiva, corretiva, etc.).
     *
     * @param tipo tipo de manutenção
     * @return lista de manutenções correspondentes
     */
    List<ManutencaoAtivo> findByTipo(TipoManutencao tipo);

    /**
     * Retorna todas as manutenções com um status específico.
     *
     * @param status status da manutenção
     * @return lista de manutenções correspondentes
     */
    List<ManutencaoAtivo> findByStatus(StatusManutencao status);

    /**
     * Verifica se há manutenções pendentes (não concluídas) para um ativo.
     *
     * @param ativoId identificador do ativo
     * @return true se existir manutenção pendente, false caso contrário
     */
    boolean existsByAtivoIdAndStatusNot(Long ativoId, StatusManutencao status);

    // ===========================================================
    // 📅 CONSULTAS POR DATA
    // ===========================================================

    /**
     * Retorna as manutenções realizadas em uma data específica.
     *
     * @param data data da manutenção
     * @return lista de manutenções ocorridas na data informada
     */
    List<ManutencaoAtivo> findByDataManutencao(LocalDate data);

    /**
     * Retorna as manutenções realizadas dentro de um intervalo de datas.
     *
     * @param inicio data inicial do intervalo
     * @param fim    data final do intervalo
     * @return lista de manutenções realizadas no período
     */
    List<ManutencaoAtivo> findByDataManutencaoBetween(LocalDate inicio, LocalDate fim);

    // ===========================================================
    // 💰 CONSULTAS FINANCEIRAS
    // ===========================================================

    /**
     * Retorna as manutenções cujo custo seja maior que o valor informado.
     *
     * @param valor valor mínimo de custo
     * @return lista de manutenções com custo acima do parâmetro
     */
    List<ManutencaoAtivo> findByCustoGreaterThan(BigDecimal valor);

    /**
     * Retorna as manutenções cujo custo seja inferior ao valor informado.
     *
     * @param valor valor máximo de custo
     * @return lista de manutenções com custo abaixo do parâmetro
     */
    List<ManutencaoAtivo> findByCustoLessThan(BigDecimal valor);

    /**
     * Busca manutenções com custo nulo ou não informado.
     *
     * @return lista de manutenções sem custo definido
     */
    @Query("SELECT m FROM ManutencaoAtivo m WHERE m.custo IS NULL OR m.custo = 0")
    List<ManutencaoAtivo> buscarManutencoesSemCusto();

    // ===========================================================
    // 🧠 CONSULTAS CUSTOMIZADAS (JPQL)
    // ===========================================================

    /**
     * Consulta personalizada: busca manutenções abertas ou em execução.
     *
     * @return lista de manutenções pendentes de conclusão
     */
    @Query("SELECT m FROM ManutencaoAtivo m WHERE m.status IN ('ABERTA', 'EM_EXECUCAO')")
    List<ManutencaoAtivo> buscarManutencoesPendentes();

    /**
     * Consulta personalizada: busca manutenções concluídas em determinado período.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de manutenções concluídas no período
     */
    @Query("SELECT m FROM ManutencaoAtivo m WHERE m.status = 'CONCLUIDA' AND m.dataManutencao BETWEEN :inicio AND :fim")
    List<ManutencaoAtivo> buscarManutencoesConcluidasNoPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Consulta personalizada: retorna a soma total dos custos de manutenção por ativo.
     *
     * @param ativoId identificador do ativo
     * @return valor total gasto em manutenções do ativo
     */
    @Query("SELECT SUM(m.custo) FROM ManutencaoAtivo m WHERE m.ativo.id = :ativoId")
    BigDecimal calcularCustoTotalPorAtivo(Long ativoId);

    /**
     * Consulta personalizada: busca a última manutenção realizada em um ativo.
     *
     * @param ativoId identificador do ativo
     * @return a manutenção mais recente, se existir
     */
    @Query("SELECT m FROM ManutencaoAtivo m WHERE m.ativo.id = :ativoId ORDER BY m.dataManutencao DESC LIMIT 1")
    Optional<ManutencaoAtivo> buscarUltimaManutencaoPorAtivo(Long ativoId);

    /**
     * Consulta personalizada: retorna o número de manutenções realizadas por fornecedor.
     *
     * @return lista de objetos contendo o ID do fornecedor e a contagem de manutenções
     */
    @Query("SELECT m.fornecedor.id, COUNT(m) FROM ManutencaoAtivo m GROUP BY m.fornecedor.id")
    List<Object[]> contarManutencoesPorFornecedor();

    // ===========================================================
    // 📊 RELATÓRIOS E MÉTRICAS
    // ===========================================================

    /**
     * Retorna o total de manutenções por tipo.
     *
     * @return lista contendo o tipo de manutenção e sua respectiva contagem
     */
    @Query("SELECT m.tipo, COUNT(m) FROM ManutencaoAtivo m GROUP BY m.tipo")
    List<Object[]> contarManutencoesPorTipo();

    /**
     * Retorna o total de manutenções por status (para dashboards e relatórios).
     *
     * @return lista contendo o status e a contagem de manutenções
     */
    @Query("SELECT m.status, COUNT(m) FROM ManutencaoAtivo m GROUP BY m.status")
    List<Object[]> contarManutencoesPorStatus();

    /**
     * Consulta o valor médio das manutenções realizadas por tipo.
     *
     * @return lista de objetos contendo o tipo e o valor médio correspondente
     */
    @Query("SELECT m.tipo, AVG(m.custo) FROM ManutencaoAtivo m WHERE m.custo IS NOT NULL GROUP BY m.tipo")
    List<Object[]> calcularCustoMedioPorTipo();
}
