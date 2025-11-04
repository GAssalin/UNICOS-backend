package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.SetorListDTO;
import br.com.unicos.ms_empresa.dto.SetorRequest;
import br.com.unicos.ms_empresa.dto.SetorResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.Setor}.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * dos setores vinculados a um departamento.
 */
public interface SetorService {

    /**
     * Cria e salva um novo setor vinculado a um departamento.
     *
     * @param request DTO contendo os dados do setor.
     * @return DTO representando o setor criado.
     */
    SetorResponse salvar(SetorRequest request);

    /**
     * Atualiza os dados de um setor existente.
     *
     * @param id      ID do setor a ser atualizado.
     * @param request DTO contendo os novos dados.
     * @return DTO representando o setor atualizado.
     */
    SetorResponse atualizar(Long id, SetorRequest request);

    /**
     * Busca um setor pelo seu ID.
     *
     * @param id ID do setor.
     * @return Optional contendo o setor, se encontrado.
     */
    Optional<SetorResponse> buscarPorId(Long id);

    /**
     * Lista todos os setores cadastrados no sistema.
     *
     * @return Lista de setores.
     */
    List<SetorListDTO> listarTodos();

    /**
     * Lista todos os setores ativos.
     *
     * @return Lista de setores com o campo "ativo" igual a true.
     */
    List<SetorListDTO> listarAtivos();

    /**
     * Lista todos os setores inativos.
     *
     * @return Lista de setores com o campo "ativo" igual a false.
     */
    List<SetorListDTO> listarInativos();

    /**
     * Lista todos os setores vinculados a um departamento específico.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores pertencentes ao departamento informado.
     */
    List<SetorListDTO> listarPorDepartamento(Long departamentoId);

    /**
     * Lista todos os setores ativos vinculados a um departamento específico.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores ativos do departamento informado.
     */
    List<SetorListDTO> listarPorDepartamentoAtivos(Long departamentoId);

    /**
     * Busca setores cujo nome contenha um determinado termo.
     *
     * @param nome Termo parcial do nome do setor.
     * @return Lista de setores correspondentes.
     */
    List<SetorListDTO> buscarPorNome(String nome);

    /**
     * Busca setores cujo nome contenha o termo informado
     * e que estejam ativos.
     *
     * @param nome Termo parcial do nome do setor.
     * @return Lista de setores ativos correspondentes.
     */
    List<SetorListDTO> buscarPorNomeEAtivo(String nome);

    /**
     * Lista todos os setores ordenados alfabeticamente pelo nome.
     *
     * @return Lista de setores ordenada por nome.
     */
    List<SetorListDTO> listarOrdenadosPorNome();

    /**
     * Lista todos os setores de um departamento
     * ordenados alfabeticamente pelo nome.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores do departamento ordenada por nome.
     */
    List<SetorListDTO> listarPorDepartamentoOrdenados(Long departamentoId);

    /**
     * Remove um setor do sistema.
     *
     * @param id ID do setor a ser removido.
     */
    void deletar(Long id);
}
