package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.model.FornecedorManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link FornecedorManutencao}.
 * <p>
 * Permite o gerenciamento de fornecedores de manutenção, oferecendo métodos
 * de busca por dados cadastrais, e relatórios sobre volume de serviços prestados.
 */
@Repository
public interface FornecedorManutencaoRepository extends JpaRepository<FornecedorManutencao, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Busca um fornecedor pelo nome (razão social ou fantasia).
     *
     * @param nome nome ou parte do nome do fornecedor
     * @return lista de fornecedores correspondentes
     */
    List<FornecedorManutencao> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca um fornecedor pelo CNPJ exato.
     *
     * @param cnpj número do CNPJ
     * @return fornecedor correspondente, se existir
     */
    Optional<FornecedorManutencao> findByCnpj(String cnpj);

    /**
     * Verifica se já existe um fornecedor com o CNPJ informado.
     *
     * @param cnpj número do CNPJ
     * @return true se existir, false caso contrário
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Busca fornecedores que tenham o mesmo e-mail de contato.
     *
     * @param email e-mail de contato
     * @return lista de fornecedores com o mesmo e-mail
     */
    List<FornecedorManutencao> findByEmailIgnoreCase(String email);

    /**
     * Busca fornecedores por telefone de contato.
     *
     * @param telefone número do telefone
     * @return lista de fornecedores encontrados
     */
    List<FornecedorManutencao> findByTelefone(String telefone);

    // ===========================================================
    // 📦 CONSULTAS DE RELACIONAMENTO
    // ===========================================================

    /**
     * Busca fornecedores que possuam manutenções associadas.
     *
     * @return lista de fornecedores com registros de manutenção
     */
    @Query("SELECT DISTINCT f FROM FornecedorManutencao f JOIN f.manutencoes m")
    List<FornecedorManutencao> buscarFornecedoresComManutencoes();

    /**
     * Busca fornecedores que ainda não possuem manutenções associadas.
     *
     * @return lista de fornecedores sem histórico de manutenção
     */
    @Query("SELECT f FROM FornecedorManutencao f WHERE f.manutencoes IS EMPTY")
    List<FornecedorManutencao> buscarFornecedoresSemManutencoes();

    /**
     * Busca o fornecedor com maior número de manutenções realizadas.
     *
     * @return lista de objetos contendo o fornecedor e a contagem de manutenções
     */
    @Query("SELECT f, COUNT(m) FROM FornecedorManutencao f LEFT JOIN f.manutencoes m GROUP BY f ORDER BY COUNT(m) DESC")
    List<Object[]> buscarFornecedoresMaisAtivos();

    /**
     * Retorna fornecedores com mais de X manutenções realizadas.
     *
     * @param quantidade número mínimo de manutenções
     * @return lista de fornecedores que atendem ao critério
     */
    @Query("SELECT f FROM FornecedorManutencao f LEFT JOIN f.manutencoes m GROUP BY f HAVING COUNT(m) >= :quantidade")
    List<FornecedorManutencao> buscarFornecedoresComMaisDe(int quantidade);

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna o total de fornecedores cadastrados no sistema.
     *
     * @return número total de fornecedores
     */
    @Query("SELECT COUNT(f) FROM FornecedorManutencao f")
    long contarTotalFornecedores();

    /**
     * Retorna o número de manutenções associadas a cada fornecedor.
     *
     * @return lista de objetos contendo o nome do fornecedor e o número de manutenções
     */
    @Query("SELECT f.nome, COUNT(m) FROM FornecedorManutencao f LEFT JOIN f.manutencoes m GROUP BY f.nome")
    List<Object[]> contarManutencoesPorFornecedor();

    /**
     * Retorna fornecedores com e-mail inválido (sem '@' ou formato incorreto).
     *
     * @return lista de fornecedores com e-mail potencialmente incorreto
     */
    @Query("SELECT f FROM FornecedorManutencao f WHERE f.email IS NOT NULL AND f.email NOT LIKE '%@%'")
    List<FornecedorManutencao> buscarFornecedoresComEmailInvalido();

    /**
     * Retorna fornecedores que ainda não possuem telefone cadastrado.
     *
     * @return lista de fornecedores sem telefone informado
     */
    @Query("SELECT f FROM FornecedorManutencao f WHERE f.telefone IS NULL OR f.telefone = ''")
    List<FornecedorManutencao> buscarFornecedoresSemTelefone();

    /**
     * Consulta personalizada: busca fornecedores que realizaram manutenções recentemente (últimos 90 dias).
     *
     * @return lista de fornecedores ativos recentemente
     */
    @Query("SELECT DISTINCT f FROM FornecedorManutencao f JOIN f.manutencoes m " +
            "WHERE m.dataManutencao >= CURRENT_DATE - 90 ORDER BY f.nome ASC")
    List<FornecedorManutencao> buscarFornecedoresRecentes();
}
