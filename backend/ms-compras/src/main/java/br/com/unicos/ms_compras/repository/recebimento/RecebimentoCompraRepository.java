package br.com.unicos.ms_compras.repository.recebimento;

import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;
import br.com.unicos.ms_compras.model.recebimento.RecebimentoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pela persistência e consultas de {@link RecebimentoCompra}.
 */
@Repository
public interface RecebimentoCompraRepository extends JpaRepository<RecebimentoCompra, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Busca um recebimento pelo seu código identificador (único).
     *
     * @param codigo código do recebimento
     * @return recebimento correspondente, se existir
     */
    Optional<RecebimentoCompra> findByCodigo(String codigo);

    /**
     * Verifica se já existe um recebimento com o código informado.
     *
     * @param codigo código do recebimento
     * @return true se existir, false caso contrário
     */
    boolean existsByCodigo(String codigo);

    /**
     * Lista todos os recebimentos associados a um pedido de compra específico.
     *
     * @param pedidoCompraId ID do pedido de compra
     * @return lista de recebimentos
     */
    List<RecebimentoCompra> findByPedidoCompraId(Long pedidoCompraId);

    /**
     * Lista todos os recebimentos associados a uma nota fiscal.
     *
     * @param notaFiscalCompraId ID da nota fiscal
     * @return lista de recebimentos
     */
    List<RecebimentoCompra> findByNotaFiscalCompraId(Long notaFiscalCompraId);

    // -----------------------------------------------------------------------
    // Filtros de domínio
    // -----------------------------------------------------------------------

    /**
     * Lista todos os recebimentos de um determinado tipo (total, parcial, devolvido etc.).
     *
     * @param tipoRecebimento tipo do recebimento
     * @return lista de recebimentos
     */
    List<RecebimentoCompra> findByTipoRecebimento(TipoRecebimento tipoRecebimento);

    /**
     * Lista todos os recebimentos em um determinado status.
     *
     * @param status status do recebimento
     * @return lista de recebimentos
     */
    List<RecebimentoCompra> findByStatus(StatusRecebimentoCompra status);

    /**
     * Lista recebimentos dentro de um intervalo de datas de recebimento.
     *
     * @param inicio data inicial (inclusive)
     * @param fim    data final (inclusive)
     * @return lista de recebimentos no período
     */
    List<RecebimentoCompra> findByDataRecebimentoBetween(LocalDate inicio, LocalDate fim);

    // -----------------------------------------------------------------------
    // Consultas paginadas e textuais
    // -----------------------------------------------------------------------

    /**
     * Pesquisa paginada por código ou observação (case-insensitive).
     *
     * @param termo    termo textual de busca
     * @param pageable parâmetros de paginação
     * @return página de resultados
     */
    @Query("""
           SELECT r
             FROM RecebimentoCompra r
            WHERE LOWER(r.codigo) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(r.observacao) LIKE LOWER(CONCAT('%', :termo, '%'))
           """)
    Page<RecebimentoCompra> search(String termo, Pageable pageable);

    // -----------------------------------------------------------------------
    // Contagens e métricas
    // -----------------------------------------------------------------------

    /**
     * Conta quantos recebimentos estão em um determinado status.
     *
     * @param status status do recebimento
     * @return total de registros com o status informado
     */
    long countByStatus(StatusRecebimentoCompra status);

    /**
     * Conta quantos recebimentos foram realizados em um determinado tipo.
     *
     * @param tipoRecebimento tipo do recebimento
     * @return total de registros do tipo informado
     */
    long countByTipoRecebimento(TipoRecebimento tipoRecebimento);
}
