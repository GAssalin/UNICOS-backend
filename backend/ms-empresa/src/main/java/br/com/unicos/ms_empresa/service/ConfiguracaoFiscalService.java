package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.ConfiguracaoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.ConfiguracaoEmpresaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade ConfiguracaoFiscal.
 *
 * Define os métodos para manipulação e consulta das configurações fiscais das empresas.
 */
public interface ConfiguracaoFiscalService {

    /**
     * Salva uma nova configuração fiscal para a empresa.
     *
     * @param request DTO contendo os dados da configuração fiscal.
     * @return DTO representando a configuração fiscal salva.
     */
    ConfiguracaoEmpresaResponse salvar(ConfiguracaoEmpresaRequest request);

    /**
     * Atualiza uma configuração fiscal existente.
     *
     * @param id ID da configuração fiscal a ser atualizada.
     * @param request DTO contendo os novos dados.
     * @return DTO representando a configuração fiscal atualizada.
     */
    ConfiguracaoEmpresaResponse atualizar(Long id, ConfiguracaoEmpresaRequest request);

    /**
     * Busca uma configuração fiscal pelo ID.
     *
     * @param id ID da configuração fiscal.
     * @return Optional contendo o DTO da configuração, se encontrada.
     */
    Optional<ConfiguracaoEmpresaResponse> buscarPorId(Long id);

    /**
     * Lista todas as configurações fiscais cadastradas.
     *
     * @return Lista de DTOs de configuração fiscal.
     */
    List<ConfiguracaoEmpresaResponse> listarTodas();

    /**
     * Remove uma configuração fiscal do sistema.
     *
     * @param id ID da configuração fiscal a ser deletada.
     */
    void deletar(Long id);

    /**
     * Busca uma configuração fiscal associada a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Optional contendo a configuração fiscal, se encontrada.
     */
    Optional<ConfiguracaoEmpresaResponse> buscarPorEmpresa(Long empresaId);
}