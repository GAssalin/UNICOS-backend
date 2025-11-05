package br.com.unicos.ms_estoque.service;

import br.com.unicos.ms_estoque.dto.InventarioEstoqueRequest;
import br.com.unicos.ms_estoque.dto.InventarioEstoqueResponse;
import br.com.unicos.ms_estoque.enums.StatusInventario;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * dos inventários físicos de estoque.
 */
public interface InventarioEstoqueService {

    /**
     * Abre ou registra um inventário para um determinado local de estoque.
     *
     * @param request DTO com dados do inventário (local, datas e status).
     * @return Inventário criado.
     */
    @Transactional
    InventarioEstoqueResponse salvar(InventarioEstoqueRequest request);

    /**
     * Atualiza as informações de um inventário existente.
     *
     * @param id      ID do inventário.
     * @param request DTO com novos dados.
     * @return Inventário atualizado.
     */
    @Transactional
    InventarioEstoqueResponse atualizar(Long id, InventarioEstoqueRequest request);

    /**
     * Exclui um inventário pelo ID.
     *
     * @param id ID do inventário.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Lista todos os inventários cadastrados.
     *
     * @return Lista de inventários.
     */
    List<InventarioEstoqueResponse> listarTodos();

    /**
     * Busca um inventário pelo ID.
     *
     * @param id ID do inventário.
     * @return Dados do inventário.
     */
    InventarioEstoqueResponse buscarPorId(Long id);

    /**
     * Lista inventários por status.
     *
     * @param status Status do inventário (ABERTO, EM_ANDAMENTO, FINALIZADO, CANCELADO).
     * @return Lista de inventários com o status informado.
     */
    List<InventarioEstoqueResponse> listarPorStatus(StatusInventario status);

    /**
     * Lista inventários iniciados entre duas datas.
     *
     * @param inicio Data/hora inicial (inclusive).
     * @param fim    Data/hora final (inclusive).
     * @return Lista de inventários no período.
     */
    List<InventarioEstoqueResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Verifica se há inventário em aberto para um local de estoque.
     *
     * @param estoqueLocalId ID do local de estoque.
     * @return {@code true} se existir inventário com status ABERTO; caso contrário, {@code false}.
     */
    boolean existeInventarioAberto(Long estoqueLocalId);
}
