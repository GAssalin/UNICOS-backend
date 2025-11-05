package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import br.com.unicos.ms_ativos.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link Ativo}.
 * <p>
 * Fornece consultas personalizadas para filtragem de ativos por tipo, status, valor e empresa,
 * além de operações básicas de CRUD herdadas do {@link JpaRepository}.
 */
@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Busca um ativo pelo seu código patrimonial único.
     *
     * @param codigoPatrimonial código único do ativo
     * @return um Optional contendo o ativo encontrado, se existir
     */
    Optional<Ativo> findByCodigoPatrimonial(String codigoPatrimonial);

    /**
     * Verifica se já existe um ativo com o código patrimonial informado.
     *
     * @param codigoPatrimonial código do ativo
     * @return true se existir, false caso contrário
     */
    boolean existsByCodigoPatrimonial(String codigoPatrimonial);

    /**
     * Retorna todos os ativos de uma determinada empresa.
     *
     * @param empresaId identificador da empresa
     * @return lista de ativos pertencentes à empresa
     */
    List<Ativo> findByEmpresaId(Long empresaId);

    /**
     * Retorna todos os ativos alocados em uma filial específica.
     *
     * @param filialId identificador da filial
     * @return lista de ativos da filial
     */
    List<Ativo> findByFilialId(Long filialId);

    /**
     * Retorna os ativos filtrados por tipo.
     *
     * @param tipo tipo de ativo (ex: EQUIPAMENTO, IMOVEL)
     * @return lista de ativos correspondentes
     */
    List<Ativo> findByTipo(TipoAtivo tipo);

    /**
     * Retorna os ativos de acordo com o status atual.
     *
     * @param status status do ativo
     * @return lista de ativos com o status informado
     */
    List<Ativo> findByStatus(StatusAtivo status);

    // ===========================================================
    // 💰 CONSULTAS FINANCEIRAS
    // ===========================================================

    /**
     * Busca os ativos cujo valor atual é maior que o valor informado.
     *
     * @param valor valor mínimo de comparação
     * @return lista de ativos com valor acima do parâmetro
     */
    List<Ativo> findByValorAtualGreaterThan(BigDecimal valor);

    /**
     * Busca os ativos cujo valor de aquisição é inferior ao valor informado.
     *
     * @param valor valor máximo de aquisição
     * @return lista de ativos com valor de aquisição menor que o informado
     */
    List<Ativo> findByValorAquisicaoLessThan(BigDecimal valor);

    /**
     * Retorna todos os ativos adquiridos entre duas datas.
     *
     * @param inicio data inicial do intervalo
     * @param fim    data final do intervalo
     * @return lista de ativos adquiridos no período
     */
    List<Ativo> findByDataAquisicaoBetween(LocalDate inicio, LocalDate fim);

    // ===========================================================
    // 🧠 CONSULTAS CUSTOMIZADAS (JPQL)
    // ===========================================================

    /**
     * Consulta personalizada: retorna os ativos por status e tipo.
     *
     * @param status status do ativo
     * @param tipo   tipo do ativo
     * @return lista de ativos filtrados por ambos os critérios
     */
    @Query("SELECT a FROM Ativo a WHERE a.status = :status AND a.tipo = :tipo")
    List<Ativo> buscarPorStatusETipo(StatusAtivo status, TipoAtivo tipo);

    /**
     * Consulta personalizada: retorna o valor total dos ativos de uma empresa.
     *
     * @param empresaId identificador da empresa
     * @return soma do valor atual de todos os ativos da empresa
     */
    @Query("SELECT SUM(a.valorAtual) FROM Ativo a WHERE a.empresaId = :empresaId")
    BigDecimal calcularValorTotalPorEmpresa(Long empresaId);

    /**
     * Consulta personalizada: busca ativos com valor depreciado em relação ao valor de aquisição.
     *
     * @return lista de ativos com valor atual inferior ao valor de aquisição
     */
    @Query("SELECT a FROM Ativo a WHERE a.valorAtual < a.valorAquisicao")
    List<Ativo> buscarAtivosDepreciados();

    /**
     * Consulta personalizada: busca ativos sem valor atual definido.
     *
     * @return lista de ativos que ainda não tiveram o valor atualizado
     */
    @Query("SELECT a FROM Ativo a WHERE a.valorAtual IS NULL")
    List<Ativo> buscarAtivosSemValorAtual();

    /**
     * Consulta personalizada: retorna ativos que estejam com status ATIVO e não possuam manutenção registrada.
     *
     * @return lista de ativos ativos sem manutenções
     */
    @Query("SELECT a FROM Ativo a WHERE a.status = 'ATIVO' AND a.manutencoes IS EMPTY")
    List<Ativo> buscarAtivosSemManutencao();

    // ===========================================================
    // 📊 CONSULTAS DE RELATÓRIO
    // ===========================================================

    /**
     * Retorna os ativos agrupados por tipo (para fins de relatórios).
     *
     * @return lista de objetos com o tipo e a contagem de ativos correspondentes
     */
    @Query("SELECT a.tipo, COUNT(a) FROM Ativo a GROUP BY a.tipo")
    List<Object[]> contarAtivosPorTipo();

    /**
     * Retorna os ativos agrupados por status (para relatórios gerenciais).
     *
     * @return lista de objetos com o status e a contagem de ativos correspondentes
     */
    @Query("SELECT a.status, COUNT(a) FROM Ativo a GROUP BY a.status")
    List<Object[]> contarAtivosPorStatus();

    /**
     * Busca ativos vinculados a um colaborador responsável.
     *
     * @param responsavelId identificador do responsável.
     * @return lista de ativos sob responsabilidade do colaborador.
     */
    List<Ativo> findByResponsavelId(Long responsavelId);
}
