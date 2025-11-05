package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.InventarioItemRequest;
import br.com.unicos.ms_estoque.dto.InventarioItemResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * dos itens pertencentes a um inventário físico.
 */
public interface InventarioItemService {

    /**
     * Insere um item no inventário (ou atualiza contagem do produto).
     *
     * @param request DTO com IDs do inventário e produto, além das quantidades.
     * @return Item criado.
     */
    @Transactional
    InventarioItemResponse salvar(InventarioItemRequest request);

    /**
     * Atualiza os dados de um item do inventário.
     *
     * @param id      ID do item do inventário.
     * @param request DTO com novos valores.
     * @return Item atualizado.
     */
    @Transactional
    InventarioItemResponse atualizar(Long id, InventarioItemRequest request);

    /**
     * Remove um item do inventário.
     *
     * @param id ID do item.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Lista todos os itens de um inventário específico.
     *
     * @param inventarioId ID do inventário.
     * @return Lista de itens vinculados.
     */
    List<InventarioItemResponse> listarPorInventario(Long inventarioId);

    /**
     * Lista os itens que possuem divergência entre quantidade contada e registrada.
     *
     * @param inventarioId ID do inventário.
     * @return Itens com divergência.
     */
    List<InventarioItemResponse> listarItensDivergentes(Long inventarioId);

    /**
     * Calcula a diferença total (contada - registrada) em um inventário.
     *
     * @param inventarioId ID do inventário.
     * @return Soma das diferenças (pode ser positiva, negativa ou zero).
     */
    Double calcularDiferencaTotal(Long inventarioId);
}
