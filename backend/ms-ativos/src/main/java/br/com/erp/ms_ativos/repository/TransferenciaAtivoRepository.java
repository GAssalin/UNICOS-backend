package br.com.erp.ms_ativos.repository;

import br.com.erp.ms_ativos.model.TransferenciaAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link TransferenciaAtivo}.
 *
 * Fornece métodos personalizados para consultas específicas de transferências de ativos,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface TransferenciaAtivoRepository extends JpaRepository<TransferenciaAtivo, Long> {

    /**
     * Lista todas as transferências realizadas para um ativo específico.
     *
     * @param ativoId ID do ativo transferido.
     * @return Lista de transferências associadas ao ativo.
     */
    List<TransferenciaAtivo> findByAtivoId(Long ativoId);

    /**
     * Lista todas as transferências originadas de uma filial específica.
     *
     * @param origemId ID da filial de origem.
     * @return Lista de transferências provenientes da filial.
     */
    List<TransferenciaAtivo> findByOrigemId(Long origemId);

    /**
     * Lista todas as transferências destinadas a uma filial específica.
     *
     * @param destinoId ID da filial de destino.
     * @return Lista de transferências destinadas à filial.
     */
    List<TransferenciaAtivo> findByDestinoId(Long destinoId);

    /**
     * Lista todas as transferências realizadas dentro de um intervalo de datas.
     *
     * @param inicio Data inicial do intervalo.
     * @param fim Data final do intervalo.
     * @return Lista de transferências realizadas no período informado.
     */
    List<TransferenciaAtivo> findByDataTransferenciaBetween(LocalDate inicio, LocalDate fim);
}