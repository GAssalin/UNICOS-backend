package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.model.core.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Pagamento}.
 * <p>
 * Fornece consultas específicas para pagamentos por status, data e vencimento.
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    /**
     * Busca pagamentos por status.
     *
     * @param status Status do pagamento (PENDENTE, PAGO, CANCELADO, etc.).
     * @return Lista de pagamentos com o status informado.
     */
    List<Pagamento> findByStatus(StatusPagamento status);

    /**
     * Busca pagamentos com vencimento até a data informada.
     *
     * @param dataVencimento Data limite de vencimento.
     * @return Lista de pagamentos com vencimento até a data especificada.
     */
    List<Pagamento> findByDataVencimentoBefore(LocalDate dataVencimento);

    /**
     * Lista pagamentos realizados em um determinado período.
     *
     * @param inicio Data inicial.
     * @param fim    Data final.
     * @return Lista de pagamentos realizados no intervalo.
     */
    @Query("SELECT p FROM Pagamento p WHERE p.dataPagamento BETWEEN :inicio AND :fim")
    List<Pagamento> buscarPagamentosRealizadosEntre(LocalDate inicio, LocalDate fim);
}
