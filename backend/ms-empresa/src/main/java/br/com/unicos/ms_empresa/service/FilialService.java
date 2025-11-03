package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.FilialListDTO;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.dto.FilialResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service responsável por operações relacionadas à entidade Filial.
 */
public interface FilialService {

    /**
     * Cria uma nova filial.
     *
     * @param request dados da filial a ser criada
     * @return dados da filial criada
     */
    FilialResponse create(FilialRequest request);

    /**
     * Atualiza uma filial existente.
     *
     * @param id identificador da filial
     * @param request dados atualizados da filial
     * @return dados da filial atualizada
     */
    FilialResponse update(Long id, FilialRequest request);

    /**
     * Retorna os detalhes completos de uma filial.
     *
     * @param id identificador da filial
     * @return detalhes da filial
     */
    FilialResponse findById(Long id);

    /**
     * Retorna uma lista simplificada das filiais de uma empresa.
     *
     * @param empresaId identificador da empresa
     * @return lista de filiais
     */
    List<FilialListDTO> findByEmpresa(Long empresaId);

    /**
     * Busca uma filial pelo CNPJ.
     *
     * @param cnpj número do CNPJ
     * @return Optional contendo os dados da filial, se encontrada
     */
    Optional<FilialResponse> findByCnpj(String cnpj);

    /**
     * Retorna as filiais localizadas em uma determinada cidade.
     *
     * @param cidade nome da cidade
     * @return lista de filiais
     */
    List<FilialListDTO> findByCidade(String cidade);

    /**
     * Retorna as filiais de uma determinada UF.
     *
     * @param uf sigla da unidade federativa (ex: SP, RJ)
     * @return lista de filiais
     */
    List<FilialListDTO> findByUf(String uf);

    /**
     * Lista todas as filiais ordenadas pela razão social.
     *
     * @return lista de filiais
     */
    List<FilialListDTO> findAllOrderedByRazaoSocial();

    /**
     * Exclui uma filial.
     *
     * @param id identificador da filial a ser excluída
     */
    void delete(Long id);
}