package br.com.unicos.ms_compras.service.fiscal;

import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemListDTO;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemRequest;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalItemResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.fiscal.NotaFiscalItem}.
 *
 * <p>
 * Controla os itens que compõem as notas fiscais de compra,
 * permitindo cálculos e conferência de tributos e valores.
 * </p>
 */
public interface NotaFiscalItemService {

    /**
     * Cria um novo item vinculado a uma nota fiscal de compra.
     *
     * @param request dados do item
     * @return item criado
     */
    NotaFiscalItemResponse criar(NotaFiscalItemRequest request);

    /**
     * Atualiza um item de nota fiscal existente.
     *
     * @param id      identificador do item
     * @param request dados atualizados
     * @return item atualizado
     */
    NotaFiscalItemResponse atualizar(Long id, NotaFiscalItemRequest request);

    /**
     * Busca um item de nota fiscal pelo identificador.
     *
     * @param id identificador do item
     * @return item correspondente, se existir
     */
    Optional<NotaFiscalItemResponse> buscarPorId(Long id);

    /**
     * Lista todos os itens de uma nota fiscal específica.
     *
     * @param notaFiscalCompraId ID da nota fiscal
     * @return lista de itens
     */
    List<NotaFiscalItemListDTO> listarPorNotaFiscal(Long notaFiscalCompraId);

    /**
     * Lista todos os itens referentes a um produto específico.
     *
     * @param produtoId ID do produto
     * @return lista de itens
     */
    List<NotaFiscalItemListDTO> listarPorProduto(Long produtoId);

    /**
     * Exclui um item de nota fiscal.
     *
     * @param id identificador do item
     */
    void deletar(Long id);
}
