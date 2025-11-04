package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.ConfiguracaoFiscalRequest;
import br.com.unicos.ms_empresa.dto.ConfiguracaoFiscalResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.ConfiguracaoFiscal}.
 * <p>
 * Define os métodos para manipulação e consulta das configurações fiscais das empresas.
 */
public interface ConfiguracaoFiscalService {

    /**
     * Cria e salva uma nova configuração fiscal para a empresa.
     *
     * @param request DTO contendo os dados da configuração fiscal.
     * @return DTO representando a configuração fiscal salva.
     */
    ConfiguracaoFiscalResponse salvar(ConfiguracaoFiscalRequest request);

    /**
     * Atualiza uma configuração fiscal existente.
     *
     * @param id      ID da configuração fiscal a ser atualizada.
     * @param request DTO contendo os novos dados.
     * @return DTO representando a configuração fiscal atualizada.
     */
    ConfiguracaoFiscalResponse atualizar(Long id, ConfiguracaoFiscalRequest request);

    /**
     * Busca uma configuração fiscal pelo ID.
     *
     * @param id ID da configuração fiscal.
     * @return Optional contendo o DTO da configuração, se encontrada.
     */
    Optional<ConfiguracaoFiscalResponse> buscarPorId(Long id);

    /**
     * Lista todas as configurações fiscais cadastradas.
     *
     * @return Lista de DTOs de configuração fiscal.
     */
    List<ConfiguracaoFiscalResponse> listarTodas();

    /**
     * Lista todas as configurações fiscais ativas.
     *
     * @return Lista de configurações fiscais com status ativo = true.
     */
    List<ConfiguracaoFiscalResponse> listarAtivas();

    /**
     * Lista todas as configurações fiscais inativas.
     *
     * @return Lista de configurações fiscais com status ativo = false.
     */
    List<ConfiguracaoFiscalResponse> listarInativas();

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
     * @return Optional contendo o DTO da configuração, se encontrada.
     */
    Optional<ConfiguracaoFiscalResponse> buscarPorEmpresa(Long empresaId);
}
