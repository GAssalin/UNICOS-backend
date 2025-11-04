package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.EmpresaRequest;
import br.com.unicos.ms_empresa.dto.EmpresaResponse;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.FilialRequest;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.Empresa}.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * das empresas cadastradas no sistema.
 */
public interface EmpresaService {

    /**
     * Cria e salva uma nova empresa, gerando automaticamente
     * sua filial matriz e endereço principal.
     *
     * @param request         DTO contendo os dados da empresa.
     * @param enderecoRequest DTO contendo os dados do endereço principal.
     * @param filialRequest   DTO contendo os dados da filial matriz.
     * @return DTO representando a empresa salva.
     */
    EmpresaResponse salvar(EmpresaRequest request, EnderecoEmpresaRequest enderecoRequest, FilialRequest filialRequest);

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * @param id      ID da empresa a ser atualizada.
     * @param request DTO contendo os novos dados da empresa.
     * @return DTO representando a empresa atualizada.
     */
    EmpresaResponse atualizar(Long id, EmpresaRequest request);

    /**
     * Busca uma empresa pelo seu ID.
     *
     * @param id ID da empresa.
     * @return Optional contendo a empresa, se encontrada.
     */
    Optional<EmpresaResponse> buscarPorId(Long id);

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa.
     * @return Optional contendo a empresa, se encontrada.
     */
    Optional<EmpresaResponse> buscarPorCnpj(String cnpj);

    /**
     * Lista todas as empresas cadastradas.
     *
     * @return Lista de empresas.
     */
    List<EmpresaResponse> listarTodas();

    /**
     * Lista todas as empresas ordenadas alfabeticamente pela razão social.
     *
     * @return Lista de empresas ordenadas.
     */
    List<EmpresaResponse> listarOrdenadasPorRazaoSocial();

    /**
     * Busca empresas cuja razão social contenha um termo específico.
     *
     * @param razaoSocial Termo parcial da razão social.
     * @return Lista de empresas correspondentes ao termo informado.
     */
    List<EmpresaResponse> buscarPorRazaoSocial(String razaoSocial);

    /**
     * Busca empresas cujo nome fantasia contenha um termo específico.
     *
     * @param nomeFantasia Termo parcial do nome fantasia.
     * @return Lista de empresas correspondentes ao termo informado.
     */
    List<EmpresaResponse> buscarPorNomeFantasia(String nomeFantasia);

    /**
     * Lista todas as empresas que possuem inscrição estadual cadastrada.
     *
     * @return Lista de empresas com inscrição estadual.
     */
    List<EmpresaResponse> listarComInscricaoEstadual();

    /**
     * Lista todas as empresas que possuem inscrição municipal cadastrada.
     *
     * @return Lista de empresas com inscrição municipal.
     */
    List<EmpresaResponse> listarComInscricaoMunicipal();

    /**
     * Remove uma empresa do sistema.
     *
     * @param id ID da empresa a ser removida.
     */
    void deletar(Long id);
}
