package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.MovimentacaoLoteRequest;
import br.com.unicos.ms_estoque.dto.MovimentacaoLoteResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas às movimentações de lotes de produtos.
 */
public interface MovimentacaoLoteService {

    /**
     * Cria um vínculo de lote com uma movimentação de estoque.
     *
     * @param request DTO com IDs da movimentação e do lote, além da quantidade.
     * @return Dados persistidos da movimentação de lote.
     */
    @Transactional
    MovimentacaoLoteResponse salvar(MovimentacaoLoteRequest request);

    /**
     * Exclui um vínculo de lote de uma movimentação.
     *
     * @param id ID do registro de movimentação de lote.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Lista todos os vínculos de lote associados a uma movimentação.
     *
     * @param movimentacaoId ID da movimentação de estoque.
     * @return Lista de movimentações de lote.
     */
    List<MovimentacaoLoteResponse> listarPorMovimentacao(Long movimentacaoId);

    /**
     * Lista todas as movimentações registradas para um determinado lote.
     *
     * @param loteId ID do lote.
     * @return Lista de movimentações referentes ao lote informado.
     */
    List<MovimentacaoLoteResponse> listarPorLote(Long loteId);

    /**
     * Calcula a soma total movimentada para um lote específico.
     *
     * @param loteId ID do lote.
     * @return Quantidade total movimentada (pode ser positiva, negativa ou zero).
     */
    Double calcularQuantidadeMovimentadaPorLote(Long loteId);
}
