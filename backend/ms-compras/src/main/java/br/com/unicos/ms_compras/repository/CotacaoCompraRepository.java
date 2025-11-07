package br.com.unicos.ms_compras.repository;

import br.com.unicos.ms_compras.enums.StatusCotacao;
import br.com.unicos.ms_compras.enums.TipoCotacao;
import br.com.unicos.ms_compras.model.cotacao.CotacaoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pelo acesso e manipulação dos dados de {@link CotacaoCompra}.
 */
@Repository
public interface CotacaoCompraRepository extends JpaRepository<CotacaoCompra, Long> {

    // -----------------------------------------------------------------------
    // Identificadores de negócio
    // -----------------------------------------------------------------------

    /**
     * Busca uma cotação pelo seu código único.
     *
     * @param codigo código da cotação (único no domínio)
     * @return Optional contendo a cotação, se encontrada
     */
    Optional<CotacaoCompra> findByCodigo(String codigo);

    /**
     * Verifica se já existe uma cotação com o código informado.
     *
     * @param codigo código da cotação
     * @return true se existir, false caso contrário
     */
    boolean existsByCodigo(String codigo);

    // -----------------------------------------------------------------------
    // Filtros de domínio (status, tipo, datas)
    // -----------------------------------------------------------------------

    /**
     * Lista cotações por status, com paginação.
     *
     * @param status   status da cotação
     * @param pageable informações de paginação/ordenação
     * @return página de cotações
     */
    Page<CotacaoCompra> findByStatus(StatusCotacao status, Pageable pageable);

    /**
     * Lista cotações por tipo, com paginação.
     *
     * @param tipoCotacao tipo da cotação
     * @param pageable    informações de paginação/ordenação
     * @return página de cotações
     */
    Page<CotacaoCompra> findByTipoCotacao(TipoCotacao tipoCotacao, Pageable pageable);

    /**
     * Lista cotações abertas dentro de um intervalo de datas.
     *
     * @param inicio data inicial (inclusive)
     * @param fim    data final (inclusive)
     * @return lista de cotações no período
     */
    List<CotacaoCompra> findByDataAberturaBetween(LocalDate inicio, LocalDate fim);

    // -----------------------------------------------------------------------
    // Pesquisas textuais (código/observação) – case-insensitive
    // -----------------------------------------------------------------------

    /**
     * Pesquisa paginada por termo livre em código ou observação (case-insensitive).
     * Útil para telas de busca.
     *
     * @param termo    termo de busca
     * @param pageable paginação/ordenação
     * @return página de resultados
     */
    @Query("""
            SELECT c
              FROM CotacaoCompra c
             WHERE (LOWER(c.codigo) LIKE LOWER(CONCAT('%', :termo, '%'))
                 OR LOWER(c.observacao) LIKE LOWER(CONCAT('%', :termo, '%')))
            """)
    Page<CotacaoCompra> searchByCodigoOrObservacao(String termo, Pageable pageable);

    // -----------------------------------------------------------------------
    // Métricas/contagens rápidas para dashboards
    // -----------------------------------------------------------------------

    /**
     * Conta cotações por status.
     *
     * @param status status da cotação
     * @return total no status informado
     */
    long countByStatus(StatusCotacao status);

    /**
     * Conta cotações por tipo.
     *
     * @param tipo tipo da cotação
     * @return total no tipo informado
     */
    long countByTipoCotacao(TipoCotacao tipo);
}
