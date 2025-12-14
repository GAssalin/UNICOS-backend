package br.com.unicos.ms_produtos.service.interfaces;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas
 * ao histórico de alterações de preço dos produtos.
 *
 * <p>
 * Permite rastreabilidade financeira, auditoria e análises
 * de variação de preços ao longo do tempo.
 * </p>
 */
public interface HistoricoPrecoService {

    /**
     * Registra um novo histórico de preço para um produto.
     *
     * @param produtoId ID do produto.
     * @param request   Dados do histórico.
     * @return DTO representando o registro criado.
     */
    HistoricoPrecoResponse salvar(Long produtoId, HistoricoPrecoRequest request);

    /**
     * Busca um registro de histórico de preço pelo ID.
     *
     * @param id Identificador do histórico.
     * @return DTO do histórico, se encontrado.
     */
    Optional<HistoricoPrecoResponse> buscarPorId(Long id);

    /**
     * Lista todos os históricos de preço cadastrados,
     * ordenados do mais recente para o mais antigo.
     *
     * @return Lista completa de históricos.
     */
    List<HistoricoPrecoResponse> listarTodos();

    /**
     * Lista todos os históricos de preço de um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de históricos do produto.
     */
    List<HistoricoPrecoResponse> listarPorProduto(Long produtoId);

    /**
     * Lista os últimos 10 registros de histórico de preço de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista resumida dos últimos registros.
     */
    List<HistoricoPrecoListDTO> listarUltimosPorProduto(Long produtoId);

    /**
     * Remove um registro de histórico de preço.
     *
     * @param id Identificador do histórico.
     */
    void deletar(Long id);
}
