package br.com.unicos.ms_pagamento.repository;

import br.com.unicos.ms_pagamento.model.FormaPagamento;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade FormaPagamento.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

    /**
     * Lista todas as formas de pagamento que estão ativas.
     *
     * @return Lista de formas de pagamento com o campo "ativo" igual a true.
     */
    List<FormaPagamento> findByAtivoTrue();

    /**
     * Busca todas as formas de pagamento de um tipo específico.
     *
     * @param tipo Tipo da forma de pagamento (DINHEIRO, PIX, CARTÃO, etc).
     * @return Lista de formas de pagamento do tipo informado.
     */
    List<FormaPagamento> findByTipo(TipoFormaPagamento tipo);

    /**
     * Busca formas de pagamento cuja descrição contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param descricao Termo de busca parcial.
     * @return Lista de formas de pagamento correspondentes ao termo informado.
     */
    List<FormaPagamento> findByDescricaoContainingIgnoreCase(String descricao);
}