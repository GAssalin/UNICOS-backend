package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.ColaboradorRequest;
import br.com.unicos.ms_pessoas.dto.ColaboradorResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para gestão de colaboradores.
 */
public interface ColaboradorService {

    /**
     * Cria colaborador.
     *
     * @param request dados do colaborador
     * @return colaborador criado
     */
    ColaboradorResponse salvar(ColaboradorRequest request);

    /**
     * Atualiza colaborador.
     *
     * @param id      ID do colaborador
     * @param request dados atualizados
     * @return colaborador atualizado
     */
    ColaboradorResponse atualizar(Long id, ColaboradorRequest request);

    /**
     * Lista colaboradores por empresa.
     *
     * @param empresaId ID da empresa (ms-empresa)
     * @return lista de colaboradores
     */
    List<ColaboradorResponse> listarPorEmpresa(Long empresaId);

    /**
     * Busca colaborador por ID.
     *
     * @param id ID do colaborador
     * @return colaborador (se encontrado)
     */
    Optional<ColaboradorResponse> buscarPorId(Long id);

    /**
     * Exclui colaborador por ID.
     *
     * @param id ID do colaborador
     */
    void excluir(Long id);
}
