package br.com.erp.ms_empresa.service;

import br.com.erp.ms_empresa.dto.EnderecoEmpresaListDTO;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaResponse;
import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;

import java.util.List;
import java.util.Optional;

/**
 * Service responsável pelas operações relacionadas à entidade EnderecoEmpresa.
 */
public interface EnderecoEmpresaService {

    /**
     * Cria um novo endereço empresarial.
     *
     * @param request dados do endereço a ser criado
     * @return dados do endereço criado
     */
    EnderecoEmpresaResponse create(EnderecoEmpresaRequest request);

    /**
     * Atualiza um endereço empresarial existente.
     *
     * @param id identificador do endereço
     * @param request dados atualizados do endereço
     * @return dados do endereço atualizado
     */
    EnderecoEmpresaResponse update(Long id, EnderecoEmpresaRequest request);

    /**
     * Busca um endereço empresarial pelo seu ID.
     *
     * @param id identificador do endereço
     * @return dados detalhados do endereço
     */
    EnderecoEmpresaResponse findById(Long id);

    /**
     * Lista todos os endereços vinculados a uma empresa.
     *
     * @param empresaId identificador da empresa
     * @return lista de endereços
     */
    List<EnderecoEmpresaListDTO> findByEmpresa(Long empresaId);

    /**
     * Busca um endereço pelo CEP.
     *
     * @param cep código postal
     * @return Optional contendo o endereço, se encontrado
     */
    Optional<EnderecoEmpresaResponse> findByCep(String cep);

    /**
     * Lista endereços de uma cidade específica.
     *
     * @param cidade nome da cidade
     * @return lista de endereços
     */
    List<EnderecoEmpresaListDTO> findByCidade(String cidade);

    /**
     * Lista endereços de uma determinada UF.
     *
     * @param uf sigla da unidade federativa (ex: SP, RJ)
     * @return lista de endereços
     */
    List<EnderecoEmpresaListDTO> findByUf(String uf);

    /**
     * Lista endereços de um tipo específico (ex: MATRIZ, FATURAMENTO, ENTREGA).
     *
     * @param tipo tipo de endereço
     * @return lista de endereços
     */
    List<EnderecoEmpresaListDTO> findByTipo(TipoEnderecoEmpresa tipo);

    /**
     * Exclui um endereço empresarial.
     *
     * @param id identificador do endereço a ser excluído
     */
    void delete(Long id);
}