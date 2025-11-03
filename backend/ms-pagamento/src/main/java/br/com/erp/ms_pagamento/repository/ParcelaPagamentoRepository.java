package br.com.erp.ms_pagamento.repository;

import br.com.erp.ms_pagamento.model.ParcelaPagamento;
import br.com.erp.ms_pagamento.enums.StatusParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade ParcelaPagamento.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface ParcelaPagamentoRepository extends JpaRepository<ParcelaPagamento, Long> {

    /**
     * Lista todas as parcelas com um determinado status.
     *
     * @param status Status da parcela (PENDENTE, QUITADA, ATRASADA).
     * @return Lista de parcelas com o status informado.
     */
    List<ParcelaPagamento> findByStatus(StatusParcela status);

    /**
     * Lista todas as parcelas associadas a um pagamento específico.
     *
     * @param pagamentoId ID do pagamento.
     * @return Lista de parcelas vinculadas ao pagamento informado.
     */
    List<ParcelaPagamento> findByPagamentoId(Long pagamentoId);

    /**
     * Lista todas as parcelas com vencimento anterior à data informada.
     *
     * @param data Data limite de vencimento.
     * @return Lista de parcelas vencidas antes da data informada.
     */
    List<ParcelaPagamento> findByDataVencimentoBefore(LocalDate data);

    /**
     * Busca todas as parcelas atrasadas (vencidas e não quitadas).
     *
     * @return Lista de parcelas em atraso.
     */
    @Query("SELECT p FROM ParcelaPagamento p WHERE p.dataVencimento < CURRENT_DATE AND p.status <> 'QUITADA'")
    List<ParcelaPagamento> buscarParcelasAtrasadas();

    /**
     * Conta quantas parcelas estão atualmente pendentes.
     *
     * @return Quantidade de parcelas com status PENDENTE.
     */
    @Query("SELECT COUNT(p) FROM ParcelaPagamento p WHERE p.status = 'PENDENTE'")
    Long contarParcelasPendentes();
}