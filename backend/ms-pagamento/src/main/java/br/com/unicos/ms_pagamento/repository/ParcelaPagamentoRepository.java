package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.ms_pagamento.enums.StatusParcela;
import br.com.unicos.ms_pagamento.model.core.ParcelaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ParcelaPagamento}.
 * <p>
 * Permite consultas por status, data e vínculo com pagamentos.
 */
@Repository
public interface ParcelaPagamentoRepository extends JpaRepository<ParcelaPagamento, Long> {

    /**
     * Busca parcelas por status.
     *
     * @param status Status da parcela (PENDENTE, PAGA, ATRASADA, etc.).
     * @return Lista de parcelas no status informado.
     */
    List<ParcelaPagamento> findByStatus(StatusParcela status);

    /**
     * Busca parcelas com vencimento até a data informada.
     *
     * @param dataVencimento Data limite.
     * @return Lista de parcelas vencidas até a data especificada.
     */
    List<ParcelaPagamento> findByDataVencimentoBefore(LocalDate dataVencimento);
}
