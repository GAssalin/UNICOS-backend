package br.com.unicos.ms_compras.service.requisicao;

import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemListDTO;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemRequest;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoItemResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.requisicao.RequisicaoItem}.
 *
 * <p>
 * Controla os itens solicitados em cada requisição de compra,
 * permitindo acompanhamento do atendimento e priorização de demanda.
 * </p>
 */
public interface RequisicaoItemService {

    /**
     * Cria um novo item dentro de uma requisição.
     *
     * @param request dados do item solicitado
     * @return item criado
     */
    RequisicaoItemResponse criar(RequisicaoItemRequest request);

    /**
     * Atualiza um item de requisição existente.
     *
     * @param id      identificador do item
     * @param request dados atualizados
     * @return item atualizado
     */
    RequisicaoItemResponse atualizar(Long id, RequisicaoItemRequest request);

    /**
     * Busca um item de requisição pelo identificador.
     *
     * @param id identificador do item
     * @return item correspondente, se existir
     */
    Optional<RequisicaoItemResponse> buscarPorId(Long id);

    /**
     * Lista todos os itens de uma requisição.
     *
     * @param requisicaoCompraId ID da requisição
     * @return lista de itens
     */
    List<RequisicaoItemListDTO> listarPorRequisicao(Long requisicaoCompraId);

    /**
     * Lista todos os itens de um determinado produto.
     *
     * @param produtoId ID do produto
     * @return lista de itens solicitados com esse produto
     */
    List<RequisicaoItemListDTO> listarPorProduto(Long produtoId);

    /**
     * Exclui um item de requisição.
     *
     * @param id identificador do item
     */
    void deletar(Long id);
}
