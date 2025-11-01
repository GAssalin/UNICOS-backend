package br.com.erp.ms_ativos.service;

import br.com.erp.ms_ativos.dto.AtivoRequest;
import br.com.erp.ms_ativos.dto.AtivoResponse;
import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade Ativo.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * dos ativos cadastrados no sistema.
 */
public interface AtivoService {

    /**
     * Salva um novo ativo.
     *
     * @param request DTO contendo os dados do ativo.
     * @return DTO representando o ativo salvo.
     */
    AtivoResponse salvar(AtivoRequest request);

    /**
     * Atualiza os dados de um ativo existente.
     *
     * @param id      ID do ativo a ser atualizado.
     * @param request DTO contendo os novos dados do ativo.
     * @return DTO representando o ativo atualizado.
     */
    AtivoResponse atualizar(Long id, AtivoRequest request);

    /**
     * Busca um ativo pelo ID.
     *
     * @param id ID do ativo.
     * @return Optional contendo o DTO do ativo, se encontrado.
     */
    Optional<AtivoResponse> buscarPorId(Long id);

    /**
     * Busca um ativo pelo código patrimonial.
     *
     * @param codigoPatrimonial Código patrimonial único.
     * @return Optional contendo o DTO do ativo, se encontrado.
     */
    Optional<AtivoResponse> buscarPorCodigoPatrimonial(String codigoPatrimonial);

    /**
     * Lista todos os ativos cadastrados.
     *
     * @return Lista de ativos.
     */
    List<AtivoResponse> listarTodos();

    /**
     * Lista todos os ativos de uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de ativos vinculados à empresa.
     */
    List<AtivoResponse> buscarPorEmpresa(Long empresaId);

    /**
     * Lista todos os ativos de uma filial específica.
     *
     * @param filialId ID da filial.
     * @return Lista de ativos vinculados à filial.
     */
    List<AtivoResponse> buscarPorFilial(Long filialId);

    /**
     * Lista os ativos de um tipo específico.
     *
     * @param tipo Tipo de ativo (EQUIPAMENTO, VEICULO, etc.).
     * @return Lista de ativos do tipo informado.
     */
    List<AtivoResponse> buscarPorTipo(TipoAtivo tipo);

    /**
     * Lista os ativos com um determinado status.
     *
     * @param status Status atual do ativo.
     * @return Lista de ativos com o status informado.
     */
    List<AtivoResponse> buscarPorStatus(StatusAtivo status);

    /**
     * Remove um ativo do sistema.
     *
     * @param id ID do ativo a ser removido.
     */
    void deletar(Long id);
}