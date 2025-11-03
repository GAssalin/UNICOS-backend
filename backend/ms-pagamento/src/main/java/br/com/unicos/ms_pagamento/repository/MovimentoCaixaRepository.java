package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.ms_pagamento.model.MovimentoCaixa;
import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade MovimentoCaixa.
 *
 * Fornece métodos personalizados para consultas específicas de movimentações financeiras,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface MovimentoCaixaRepository extends JpaRepository<MovimentoCaixa, Long> {

    /**
     * Lista todos os movimentos de um determinado tipo.
     *
     * @param tipoMovimento Tipo do movimento (ENTRADA ou SAIDA).
     * @return Lista de movimentos correspondentes ao tipo informado.
     */
    List<MovimentoCaixa> findByTipoMovimento(TipoMovimentoCaixa tipoMovimento);

    /**
     * Lista todos os movimentos realizados em uma data específica.
     *
     * @param dataMovimento Data do movimento.
     * @return Lista de movimentos realizados na data informada.
     */
    List<MovimentoCaixa> findByDataMovimento(LocalDate dataMovimento);

    /**
     * Calcula o valor total de todas as entradas registradas.
     *
     * @return Soma de todos os movimentos do tipo ENTRADA.
     */
    @Query("SELECT SUM(m.valor) FROM MovimentoCaixa m WHERE m.tipoMovimento = 'ENTRADA'")
    BigDecimal calcularTotalEntradas();

    /**
     * Calcula o valor total de todas as saídas registradas.
     *
     * @return Soma de todos os movimentos do tipo SAIDA.
     */
    @Query("SELECT SUM(m.valor) FROM MovimentoCaixa m WHERE m.tipoMovimento = 'SAIDA'")
    BigDecimal calcularTotalSaidas();

    /**
     * Calcula o total de entradas realizadas no dia atual.
     *
     * @return Soma dos valores de entrada registrados na data atual.
     */
    @Query("SELECT SUM(m.valor) FROM MovimentoCaixa m WHERE m.dataMovimento = CURRENT_DATE AND m.tipoMovimento = 'ENTRADA'")
    BigDecimal totalEntradasHoje();

    /**
     * Calcula o total de saídas realizadas no dia atual.
     *
     * @return Soma dos valores de saída registrados na data atual.
     */
    @Query("SELECT SUM(m.valor) FROM MovimentoCaixa m WHERE m.dataMovimento = CURRENT_DATE AND m.tipoMovimento = 'SAIDA'")
    BigDecimal totalSaidasHoje();
}