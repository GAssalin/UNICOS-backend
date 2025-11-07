package br.com.unicos.ms_compras.service.recebimento;

import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemListDTO;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemRequest;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoItemResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.recebimento.RecebimentoItem}.
 *
 * <p>
 * Controla os itens recebidos em cada processo de recebimento de compra,
 * incluindo validações de quantidades previstas, recebidas e devolvidas.
 * </p>
 */
public interface RecebimentoItemService {

    /**
     * Registra um novo item de recebimento.
     *
     * @param request dados do item
     * @return item criado
     */
    RecebimentoItemResponse criar(RecebimentoItemRequest request);

    /**
     * Atualiza um item de recebimento existente.
     *
     * @param id      identificador do item
     * @param request dados atualizados
     * @return item atualizado
     */
    RecebimentoItemResponse atualizar(Long id, RecebimentoItemRequest request);

    /**
     * Busca um item de recebimento pelo identificador.
     *
     * @param id identificador do item
     * @return item correspondente, se existir
     */
    Optional<RecebimentoItemResponse> buscarPorId(Long id);

    /**
     * Lista todos os itens de um recebimento específico.
     *
     * @param recebimentoCompraId ID do recebimento
     * @return lista de itens
     */
    List<RecebimentoItemListDTO> listarPorRecebimento(Long recebimentoCompraId);

    /**
     * Lista todos os itens de um determinado produto.
     *
     * @param produtoId ID do produto
     * @return lista de itens recebidos para o produto
     */
    List<RecebimentoItemListDTO> listarPorProduto(Long produtoId);

    /**
     * Exclui um item de recebimento.
     *
     * @param id identificador do item
     */
    void deletar(Long id);
}
