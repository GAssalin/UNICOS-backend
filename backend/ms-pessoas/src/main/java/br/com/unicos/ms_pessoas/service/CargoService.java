package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.CargoRequest;
import br.com.unicos.ms_pessoas.dto.CargoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento de cargos.
 */
public interface CargoService {

    /**
     * Cria cargo.
     *
     * @param request dados do cargo
     * @return cargo criado
     */
    CargoResponse salvar(CargoRequest request);

    /**
     * Atualiza cargo.
     *
     * @param id      ID do cargo
     * @param request dados atualizados
     * @return cargo atualizado
     */
    CargoResponse atualizar(Long id, CargoRequest request);

    /**
     * Lista todos os cargos.
     *
     * @return lista de cargos
     */
    List<CargoResponse> listarTodos();

    /**
     * Busca cargo por ID.
     *
     * @param id ID do cargo
     * @return cargo (se encontrado)
     */
    Optional<CargoResponse> buscarPorId(Long id);

    /**
     * Exclui cargo por ID.
     *
     * @param id ID do cargo
     */
    void excluir(Long id);
}
