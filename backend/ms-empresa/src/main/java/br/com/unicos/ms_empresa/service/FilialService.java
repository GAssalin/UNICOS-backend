package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.FilialListDTO;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.dto.FilialResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.Filial}.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * das filiais vinculadas às empresas.
 */
public interface FilialService {

    /**
     * Cria e salva uma nova filial.
     *
     * @param request DTO contendo os dados da filial.
     * @return DTO representando a filial criada.
     */
    FilialResponse salvar(FilialRequest request);

    /**
     * Atualiza os dados de uma filial existente.
     *
     * @param id      ID da filial a ser atualizada.
     * @param request DTO contendo os novos dados.
     * @return DTO representando a filial atualizada.
     */
    FilialResponse atualizar(Long id, FilialRequest request);

    /**
     * Busca uma filial pelo seu ID.
     *
     * @param id ID da filial.
     * @return Optional contendo os dados da filial, se encontrada.
     */
    Optional<FilialResponse> buscarPorId(Long id);

    /**
     * Busca uma filial pelo seu CNPJ.
     *
     * @param cnpj CNPJ da filial.
     * @return Optional contendo os dados da filial, se encontrada.
     */
    Optional<FilialResponse> buscarPorCnpj(String cnpj);

    /**
     * Lista todas as filiais cadastradas no sistema.
     *
     * @return Lista de filiais.
     */
    List<FilialListDTO> listarTodas();

    /**
     * Lista todas as filiais vinculadas a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de filiais pertencentes à empresa informada.
     */
    List<FilialListDTO> listarPorEmpresa(Long empresaId);

    /**
     * Lista todas as filiais ativas.
     *
     * @return Lista de filiais com o campo "ativo" igual a true.
     */
    List<FilialListDTO> listarAtivas();

    /**
     * Lista todas as filiais inativas.
     *
     * @return Lista de filiais com o campo "ativo" igual a false.
     */
    List<FilialListDTO> listarInativas();

    /**
     * Lista todas as filiais ativas vinculadas a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de filiais ativas pertencentes à empresa informada.
     */
    List<FilialListDTO> listarPorEmpresaAtivas(Long empresaId);

    /**
     * Busca filiais cujo nome contenha um determinado termo.
     *
     * @param nome Termo parcial de busca.
     * @return Lista de filiais correspondentes ao termo informado.
     */
    List<FilialListDTO> buscarPorNome(String nome);

    /**
     * Lista todas as filiais ordenadas alfabeticamente pelo nome.
     *
     * @return Lista de filiais ordenadas por nome.
     */
    List<FilialListDTO> listarOrdenadasPorNome();

    /**
     * Lista todas as filiais de uma empresa ordenadas alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa.
     * @return Lista de filiais ordenadas por nome.
     */
    List<FilialListDTO> listarPorEmpresaOrdenadas(Long empresaId);

    /**
     * Remove uma filial do sistema.
     *
     * @param id ID da filial a ser removida.
     */
    void deletar(Long id);
}
