package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.TransacaoEstoqueListDTO;
import br.com.unicos.ms_estoque.dto.TransacaoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.TransacaoEstoqueResponse;
import br.com.unicos.ms_estoque.enums.TipoTransacao;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * das transações de estoque.
 */
public interface TransacaoEstoqueService {

    @Transactional
    TransacaoEstoqueResponse salvar(TransacaoEstoqueRequest request);

    @Transactional
    void excluir(Long id);

    List<TransacaoEstoqueListDTO> listarTodas();

    TransacaoEstoqueResponse buscarPorId(Long id);

    /**
     * Lista transações de um tipo específico.
     */
    List<TransacaoEstoqueListDTO> buscarPorTipo(TipoTransacao tipo);

    /**
     * Lista transações realizadas em um intervalo de datas.
     */
    List<TransacaoEstoqueListDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta transações por tipo em determinado período.
     */
    long contarPorTipoEPeriodo(TipoTransacao tipo, LocalDateTime inicio, LocalDateTime fim);
}
