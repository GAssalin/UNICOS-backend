package br.com.unicos.ms_pagamento.repository.caixa;

import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;
import br.com.unicos.ms_pagamento.model.caixa.MovimentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link MovimentoCaixa}.
 * <p>
 * Permite consultas de lançamentos financeiros e cálculo de saldos no caixa.
 */
@Repository
public interface MovimentoCaixaRepository extends JpaRepository<MovimentoCaixa, Long> {

    /**
     * Busca movimentos de caixa de um tipo específico (ENTRADA, SAÍDA, etc.).
     *
     * @param tipo Tipo de movimento.
     * @return Lista de movimentos do tipo informado.
     */
    List<MovimentoCaixa> findByTipoMovimento(TipoMovimentoCaixa tipo);

    /**
     * Retorna todos os movimentos dentro de um intervalo de datas.
     *
     * @param inicio Data inicial.
     * @param fim    Data final.
     * @return Lista de movimentos no período informado.
     */
    List<MovimentoCaixa> findByDataMovimentoBetween(LocalDate inicio, LocalDate fim);

    /**
     * Calcula o saldo total do caixa (entradas - saídas).
     *
     * @return Valor total do saldo atual do caixa.
     */
    @Query("""
            SELECT COALESCE(SUM(
                CASE WHEN m.tipoMovimento = 'ENTRADA' THEN m.valor 
                     WHEN m.tipoMovimento = 'SAIDA' THEN -m.valor 
                     ELSE 0 END
            ), 0)
            FROM MovimentoCaixa m
            """)
    BigDecimal calcularSaldoAtual();
}
