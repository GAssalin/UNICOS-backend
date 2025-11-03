package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.ms_pagamento.model.Pagamento;
import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Pagamento.
 *
 * Fornece métodos personalizados para consultas financeiras,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    /**
     * Busca todos os pagamentos com um determinado status.
     *
     * @param status Status do pagamento (PENDENTE, PAGO, CANCELADO, etc).
     * @return Lista de pagamentos com o status informado.
     */
    List<Pagamento> findByStatus(StatusPagamento status);

    /**
     * Busca um pagamento pela referência da transação (venda, compra, etc).
     *
     * @param referenciaId Identificador de referência da transação.
     * @return Optional contendo o pagamento, se encontrado.
     */
    Optional<Pagamento> findByReferenciaId(String referenciaId);

    /**
     * Busca pagamentos de acordo com o tipo de transação.
     *
     * @param tipoTransacao Tipo da transação (VENDA, COMPRA, OUTROS).
     * @return Lista de pagamentos correspondentes ao tipo informado.
     */
    List<Pagamento> findByTipoTransacao(TipoTransacao tipoTransacao);

    /**
     * Busca pagamentos com data de vencimento dentro de um intervalo.
     *
     * @param inicio Data inicial do intervalo.
     * @param fim    Data final do intervalo.
     * @return Lista de pagamentos cujo vencimento esteja entre as datas informadas.
     */
    List<Pagamento> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);

    /**
     * Busca pagamentos realizados dentro de um intervalo de datas.
     *
     * @param inicio Data inicial do intervalo.
     * @param fim    Data final do intervalo.
     * @return Lista de pagamentos realizados no período informado.
     */
    List<Pagamento> findByDataPagamentoBetween(LocalDate inicio, LocalDate fim);

    /**
     * Calcula o valor total de todos os pagamentos quitados.
     *
     * @return Soma total dos pagamentos com status PAGO.
     */
    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.status = 'PAGO'")
    BigDecimal totalPago();

    /**
     * Calcula o valor total de todos os pagamentos pendentes.
     *
     * @return Soma total dos pagamentos com status PENDENTE.
     */
    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.status = 'PENDENTE'")
    BigDecimal totalPendente();

    /**
     * Conta a quantidade de pagamentos atrasados.
     *
     * @return Número de pagamentos com vencimento anterior à data atual e não pagos.
     */
    @Query("SELECT COUNT(p) FROM Pagamento p WHERE p.dataVencimento < CURRENT_DATE AND p.status <> 'PAGO'")
    Long countPagamentosAtrasados();
}