package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.enums.TipoTransacao;
import br.com.unicos.ms_estoque.model.TransacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoEstoqueRepository extends JpaRepository<TransacaoEstoque, Long> {

    List<TransacaoEstoque> findByTipo(TipoTransacao tipo);

    /**
     * Busca transações realizadas em um intervalo de datas.
     */
    List<TransacaoEstoque> findByDataBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca transações de um determinado usuário responsável.
     */
    List<TransacaoEstoque> findByUsuarioResponsavelIgnoreCase(String usuarioResponsavel);

    /**
     * Consulta genérica por palavra-chave na observação.
     */
    List<TransacaoEstoque> findByObservacaoContainingIgnoreCase(String termo);

    /**
     * Conta quantas transações de determinado tipo ocorreram no período.
     */
    @Query("""
            SELECT COUNT(t) FROM TransacaoEstoque t
            WHERE t.tipo = :tipo
              AND t.data BETWEEN :inicio AND :fim
            """)
    long countByTipoAndPeriodo(TipoTransacao tipo, LocalDateTime inicio, LocalDateTime fim);
}
