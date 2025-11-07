package br.com.unicos.ms_compras.service.cotacao;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade {@link br.com.unicos.ms_compras.model.cotacao.CotacaoItem}.
 *
 * <p>Gerencia os itens cotados pelos fornecedores e seus valores unitários e totais.</p>
 */
public interface CotacaoItemService {

    /**
     * Cria um novo item de cotação.
     *
     * @param request dados do item
     * @return item criado
     */
    CotacaoItemResponse criar(CotacaoItemRequest request);

    /**
     * Atualiza um item de cotação existente.
     *
     * @param id      identificador do item
     * @param request dados atualizados
     * @return item atualizado
     */
    CotacaoItemResponse atualizar(Long id, CotacaoItemRequest request);

    /**
     * Busca um item pelo identificador.
     *
     * @param id identificador do item
     * @return item correspondente
     */
    Optional<CotacaoItemResponse> buscarPorId(Long id);

    /**
     * Lista todos os itens vinculados a uma proposta de fornecedor.
     *
     * @param cotacaoFornecedorId ID da proposta
     * @return lista de itens
     */
    List<CotacaoItemListDTO> listarPorFornecedor(Long cotacaoFornecedorId);

    /**
     * Exclui um item de cotação.
     *
     * @param id identificador do item
     */
    void deletar(Long id);
}
