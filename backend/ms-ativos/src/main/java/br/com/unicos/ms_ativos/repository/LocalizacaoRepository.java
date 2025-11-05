package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link Localizacao}.
 * <p>
 * Fornece consultas específicas para localizar ativos dentro de uma filial,
 * buscar por bloco, andar e descrição, além de relatórios agregados.
 */
@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Busca uma localização pelo nome/descrição exata.
     *
     * @param descricao descrição da localização
     * @return uma localização, se encontrada
     */
    Optional<Localizacao> findByDescricao(String descricao);

    /**
     * Retorna todas as localizações pertencentes a uma filial específica.
     *
     * @param filialId identificador da filial
     * @return lista de localizações da filial
     */
    List<Localizacao> findByFilialId(Long filialId);

    /**
     * Retorna todas as localizações filtradas por bloco.
     *
     * @param bloco nome do bloco
     * @return lista de localizações encontradas
     */
    List<Localizacao> findByBloco(String bloco);

    /**
     * Retorna todas as localizações de um determinado andar.
     *
     * @param andar número ou nome do andar
     * @return lista de localizações no andar informado
     */
    List<Localizacao> findByAndar(String andar);

    /**
     * Verifica se já existe uma localização com o mesmo nome em uma filial.
     *
     * @param descricao descrição da localização
     * @param filialId  identificador da filial
     * @return true se existir, false caso contrário
     */
    boolean existsByDescricaoAndFilialId(String descricao, Long filialId);

    // ===========================================================
    // 🧭 CONSULTAS AVANÇADAS
    // ===========================================================

    /**
     * Retorna todas as localizações que contenham determinado texto no nome.
     *
     * @param texto parte do nome a ser pesquisada
     * @return lista de localizações cujo nome contenha o texto informado
     */
    List<Localizacao> findByDescricaoContainingIgnoreCase(String texto);

    /**
     * Busca localizações combinando bloco e andar.
     *
     * @param bloco bloco da localização
     * @param andar andar da localização
     * @return lista de localizações que correspondem aos critérios informados
     */
    List<Localizacao> findByBlocoAndAndar(String bloco, String andar);

    /**
     * Retorna todas as localizações ordenadas alfabeticamente por descrição.
     *
     * @return lista ordenada de localizações
     */
    List<Localizacao> findAllByOrderByDescricaoAsc();

    // ===========================================================
    // 📊 CONSULTAS DE RELATÓRIO
    // ===========================================================

    /**
     * Consulta personalizada que retorna a contagem de localizações por filial.
     *
     * @return lista de objetos com o ID da filial e a quantidade de localizações
     */
    @Query("SELECT l.filialId, COUNT(l) FROM Localizacao l GROUP BY l.filialId")
    List<Object[]> contarLocalizacoesPorFilial();

    /**
     * Consulta personalizada que retorna a quantidade de ativos associados por localização.
     *
     * @return lista de objetos contendo a descrição da localização e o total de ativos vinculados
     */
    @Query("SELECT l.descricao, COUNT(a) FROM Localizacao l LEFT JOIN l.ativos a GROUP BY l.descricao")
    List<Object[]> contarAtivosPorLocalizacao();

    /**
     * Retorna todas as localizações que ainda não possuem ativos vinculados.
     *
     * @return lista de localizações sem ativos
     */
    @Query("SELECT l FROM Localizacao l WHERE l.ativos IS EMPTY")
    List<Localizacao> buscarLocalizacoesSemAtivos();

    /**
     * Retorna o total de localizações cadastradas por bloco.
     *
     * @return lista de blocos com suas respectivas contagens
     */
    @Query("SELECT l.bloco, COUNT(l) FROM Localizacao l GROUP BY l.bloco")
    List<Object[]> contarLocalizacoesPorBloco();
}
