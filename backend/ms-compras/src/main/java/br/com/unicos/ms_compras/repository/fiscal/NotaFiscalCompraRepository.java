package br.com.unicos.ms_compras.repository.fiscal;

import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;
import br.com.unicos.ms_compras.model.fiscal.NotaFiscalCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pelo gerenciamento e consultas de {@link NotaFiscalCompra}.
 */
@Repository
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {

    // -----------------------------------------------------------------------
    // Identificadores únicos
    // -----------------------------------------------------------------------

    /**
     * Busca uma nota fiscal pela sua chave de acesso (única por documento).
     *
     * @param chaveAcesso chave de acesso da NFe
     * @return nota fiscal correspondente, se existir
     */
    Optional<NotaFiscalCompra> findByChaveAcesso(String chaveAcesso);

    /**
     * Verifica se já existe uma nota fiscal cadastrada com a chave informada.
     *
     * @param chaveAcesso chave de acesso da NFe
     * @return true se existir, false caso contrário
     */
    boolean existsByChaveAcesso(String chaveAcesso);

    // -----------------------------------------------------------------------
    // Consultas de domínio
    // -----------------------------------------------------------------------

    /**
     * Lista todas as notas fiscais emitidas por um fornecedor específico.
     *
     * @param fornecedorId identificador do fornecedor
     * @return lista de notas fiscais
     */
    List<NotaFiscalCompra> findByFornecedorId(Long fornecedorId);

    /**
     * Lista notas fiscais de um tipo específico.
     *
     * @param tipoNotaFiscal tipo da nota fiscal
     * @return lista de notas
     */
    List<NotaFiscalCompra> findByTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal);

    /**
     * Lista notas fiscais com um determinado status.
     *
     * @param status status da nota fiscal
     * @return lista de notas
     */
    List<NotaFiscalCompra> findByStatus(StatusNotaFiscalCompra status);

    /**
     * Lista notas fiscais emitidas dentro de um intervalo de datas.
     *
     * @param inicio data inicial (inclusive)
     * @param fim    data final (inclusive)
     * @return lista de notas fiscais no período
     */
    List<NotaFiscalCompra> findByDataEmissaoBetween(LocalDate inicio, LocalDate fim);

    // -----------------------------------------------------------------------
    // Consultas paginadas e textuais
    // -----------------------------------------------------------------------

    /**
     * Pesquisa notas fiscais por número, chave ou observação.
     *
     * @param termo    termo textual
     * @param pageable parâmetros de paginação
     * @return página de resultados
     */
    @Query("""
            SELECT n
              FROM NotaFiscalCompra n
             WHERE LOWER(n.numeroNota) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(n.chaveAcesso) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(n.observacao) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    Page<NotaFiscalCompra> search(String termo, Pageable pageable);

    // -----------------------------------------------------------------------
    // Agregações e métricas
    // -----------------------------------------------------------------------

    /**
     * Obtém o valor total das notas fiscais de um fornecedor dentro de um período.
     *
     * @param fornecedorId ID do fornecedor
     * @param inicio       data inicial
     * @param fim          data final
     * @return soma dos valores totais
     */
    @Query("""
            SELECT COALESCE(SUM(n.valorTotal), 0)
              FROM NotaFiscalCompra n
             WHERE n.fornecedorId = :fornecedorId
               AND n.dataEmissao BETWEEN :inicio AND :fim
            """)
    BigDecimal sumValorTotalByFornecedorAndPeriodo(Long fornecedorId, LocalDate inicio, LocalDate fim);

    /**
     * Conta o número de notas fiscais de um determinado tipo.
     *
     * @param tipo tipo da nota fiscal
     * @return total de notas do tipo informado
     */
    long countByTipoNotaFiscal(TipoNotaFiscal tipo);

    /**
     * Conta o número de notas fiscais em um determinado status.
     *
     * @param status status da nota fiscal
     * @return total de notas com o status informado
     */
    long countByStatus(StatusNotaFiscalCompra status);
}
