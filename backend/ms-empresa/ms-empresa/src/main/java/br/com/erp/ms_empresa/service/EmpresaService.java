package br.com.erp.ms_empresa.service;

import br.com.erp.ms_empresa.dto.EmpresaRequest;
import br.com.erp.ms_empresa.dto.EmpresaResponse;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.erp.ms_empresa.dto.FilialRequest;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade Empresa.
 *
 * Define os métodos de criação, atualização, listagem e exclusão
 * das empresas cadastradas no sistema.
 */
public interface EmpresaService {

    /**
     * Salva uma nova empresa e cria automaticamente sua filial matriz, juntamente com seu endereço principal
     *
     * @param request DTO contendo os dados da empresa.
     * @return DTO representando a empresa salva.
     */
    EmpresaResponse salvar(EmpresaRequest request, EnderecoEmpresaRequest enderecoRequest, FilialRequest filialRequest);

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * @param id ID da empresa a ser atualizada.
     * @param request DTO contendo os novos dados da empresa.
     * @return DTO representando a empresa atualizada.
     */
    EmpresaResponse atualizar(Long id, EmpresaRequest request);

    /**
     * Busca uma empresa pelo ID.
     *
     * @param id ID da empresa.
     * @return Optional contendo o DTO da empresa, se encontrada.
     */
    Optional<EmpresaResponse> buscarPorId(Long id);

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa.
     * @return Optional contendo o DTO da empresa, se encontrada.
     */
    Optional<EmpresaResponse> buscarPorCnpj(String cnpj);

    /**
     * Lista todas as empresas cadastradas.
     *
     * @return Lista de empresas.
     */
    List<EmpresaResponse> listarTodas();

    /**
     * Busca empresas cuja razão social contenha um termo específico.
     *
     * @param razaoSocial Termo de busca.
     * @return Lista de empresas correspondentes ao termo informado.
     */
    List<EmpresaResponse> buscarPorRazaoSocial(String razaoSocial);

    /**
     * Busca empresas cujo nome fantasia contenha um termo específico.
     *
     * @param nomeFantasia Termo de busca.
     * @return Lista de empresas correspondentes ao termo informado.
     */
    List<EmpresaResponse> buscarPorNomeFantasia(String nomeFantasia);

    /**
     * Exclui uma empresa do sistema.
     *
     * @param id ID da empresa a ser removida.
     */
    void deletar(Long id);
}