package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.MovimentacaoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.MovimentacaoEstoqueResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas às movimentações de produtos em estoque.
 */
public interface MovimentacaoEstoqueService {

    @Transactional
    MovimentacaoEstoqueResponse salvar(MovimentacaoEstoqueRequest request);

    @Transactional
    void excluir(Long id);

    List<MovimentacaoEstoqueResponse> listarPorTransacao(Long transacaoId);

    /**
     * Lista movimentações de um produto específico.
     */
    List<MovimentacaoEstoqueResponse> listarPorProduto(Long produtoEstoqueId);

    /**
     * Calcula a quantidade total movimentada de um produto no período.
     */
    Double calcularTotalMovimentado(Long produtoEstoqueId, LocalDateTime inicio, LocalDateTime fim);
}
