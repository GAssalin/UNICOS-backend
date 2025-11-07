package br.com.unicos.ms_compras.repository.cotacao;

import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;
import br.com.unicos.ms_compras.model.cotacao.CotacaoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pela persistência e consultas de {@link CotacaoFornecedor}.
 */
@Repository
public interface CotacaoFornecedorRepository extends JpaRepository<CotacaoFornecedor, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Busca todas as propostas de um determinado fornecedor.
     *
     * @param fornecedorId identificador do fornecedor
     * @return lista de propostas
     */
    List<CotacaoFornecedor> findByFornecedorId(Long fornecedorId);

    /**
     * Busca uma proposta específica de um fornecedor para uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @param fornecedorId    ID do fornecedor
     * @return proposta correspondente, se existir
     */
    Optional<CotacaoFornecedor> findByCotacaoCompraIdAndFornecedorId(Long cotacaoCompraId, Long fornecedorId);

    // -----------------------------------------------------------------------
    // Filtros de status
    // -----------------------------------------------------------------------

    /**
     * Retorna todas as propostas com um determinado status.
     *
     * @param status status da proposta
     * @return lista de fornecedores no status informado
     */
    List<CotacaoFornecedor> findByStatus(StatusFornecedorCotacao status);

    /**
     * Retorna todas as propostas associadas a uma cotação e com o status informado.
     *
     * @param cotacaoCompraId ID da cotação
     * @param status          status da proposta
     * @return lista de propostas
     */
    List<CotacaoFornecedor> findByCotacaoCompraIdAndStatus(Long cotacaoCompraId, StatusFornecedorCotacao status);

    // -----------------------------------------------------------------------
    // Métricas de valores
    // -----------------------------------------------------------------------

    /**
     * Obtém o menor valor total proposto entre todos os fornecedores de uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @return menor valor total proposto
     */
    @Query("""
            SELECT MIN(f.valorTotal)
              FROM CotacaoFornecedor f
             WHERE f.cotacaoCompra.id = :cotacaoCompraId
            """)
    BigDecimal findMenorValorPorCotacao(Long cotacaoCompraId);

    /**
     * Obtém o maior valor total proposto entre todos os fornecedores de uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @return maior valor total proposto
     */
    @Query("""
            SELECT MAX(f.valorTotal)
              FROM CotacaoFornecedor f
             WHERE f.cotacaoCompra.id = :cotacaoCompraId
            """)
    BigDecimal findMaiorValorPorCotacao(Long cotacaoCompraId);

    /**
     * Obtém a média dos valores totais propostos para uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @return média dos valores totais
     */
    @Query("""
            SELECT AVG(f.valorTotal)
              FROM CotacaoFornecedor f
             WHERE f.cotacaoCompra.id = :cotacaoCompraId
            """)
    BigDecimal findMediaValoresPorCotacao(Long cotacaoCompraId);

    // -----------------------------------------------------------------------
    // Contagens
    // -----------------------------------------------------------------------

    /**
     * Conta quantos fornecedores participaram de uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @return total de fornecedores participantes
     */
    long countByCotacaoCompraId(Long cotacaoCompraId);

    /**
     * Conta quantas propostas estão em um determinado status.
     *
     * @param status status da proposta
     * @return total de propostas com o status informado
     */
    long countByStatus(StatusFornecedorCotacao status);
}
