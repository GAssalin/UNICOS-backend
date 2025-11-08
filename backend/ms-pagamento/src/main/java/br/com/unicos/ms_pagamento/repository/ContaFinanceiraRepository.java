package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.core.financeiro.enums.TipoContaFinanceira;
import br.com.unicos.core.financeiro.model.ContaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositório de leitura responsável por acessar informações de {@link ContaFinanceira}
 * provenientes do módulo core-financeiro.
 *
 * <p>
 * Esse repositório deve ser utilizado apenas para consultas de referência
 * dentro do contexto de pagamento, evitando operações de escrita direta
 * em entidades do core-financeiro.
 */
@Repository
public interface ContaFinanceiraRepository extends JpaRepository<ContaFinanceira, Long> {

    /**
     * Lista todas as contas financeiras de um tipo específico.
     *
     * @param tipoConta Tipo da conta (CONTA_CORRENTE, CONTA_POUPANCA, CAIXA, etc).
     * @return Lista de contas financeiras do tipo informado.
     */
    List<ContaFinanceira> findByTipoConta(TipoContaFinanceira tipoConta);

    /**
     * Busca contas cuja descrição contenha o termo informado,
     * ignorando maiúsculas e minúsculas.
     *
     * @param descricao Termo de busca parcial.
     * @return Lista de contas que contenham o termo informado na descrição.
     */
    List<ContaFinanceira> findByDescricaoContainingIgnoreCase(String descricao);

    /**
     * Calcula o saldo total de todas as contas financeiras cadastradas.
     * <p>
     * Este método deve ser usado apenas para leitura e exibição.
     *
     * @return Soma de todos os saldos registrados nas contas.
     */
    @Query("SELECT SUM(c.saldoAtual) FROM ContaFinanceira c")
    BigDecimal calcularSaldoTotal();

    /**
     * Conta quantas contas estão com saldo negativo.
     *
     * @return Quantidade de contas com saldo inferior a zero.
     */
    @Query("SELECT COUNT(c) FROM ContaFinanceira c WHERE c.saldoAtual < 0")
    Long contarContasNegativas();
}
