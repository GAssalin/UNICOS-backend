package br.com.erp.ms_ativos.service;

import br.com.erp.ms_ativos.dto.ManutencaoAtivoRequest;
import br.com.erp.ms_ativos.dto.ManutencaoAtivoResponse;
import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade ManutencaoAtivo.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * das manutenções de ativos cadastradas no sistema.
 */
public interface ManutencaoAtivoService {

    /**
     * Salva uma nova manutenção de ativo.
     *
     * @param request DTO contendo os dados da manutenção.
     * @return DTO representando a manutenção salva.
     */
    ManutencaoAtivoResponse salvar(ManutencaoAtivoRequest request);

    /**
     * Atualiza os dados de uma manutenção existente.
     *
     * @param id      ID da manutenção a ser atualizada.
     * @param request DTO contendo os novos dados da manutenção.
     * @return DTO representando a manutenção atualizada.
     */
    ManutencaoAtivoResponse atualizar(Long id, ManutencaoAtivoRequest request);

    /**
     * Busca uma manutenção pelo ID.
     *
     * @param id ID da manutenção.
     * @return Optional contendo o DTO da manutenção, se encontrada.
     */
    Optional<ManutencaoAtivoResponse> buscarPorId(Long id);

    /**
     * Lista todas as manutenções cadastradas.
     *
     * @return Lista de manutenções.
     */
    List<ManutencaoAtivoResponse> listarTodas();

    /**
     * Lista todas as manutenções de um ativo específico.
     *
     * @param ativoId ID do ativo.
     * @return Lista de manutenções associadas ao ativo.
     */
    List<ManutencaoAtivoResponse> buscarPorAtivo(Long ativoId);

    /**
     * Lista todas as manutenções de um tipo específico.
     *
     * @param tipo Tipo de manutenção (PREVENTIVA, CORRETIVA).
     * @return Lista de manutenções do tipo informado.
     */
    List<ManutencaoAtivoResponse> buscarPorTipo(TipoManutencao tipo);

    /**
     * Lista todas as manutenções com um determinado status.
     *
     * @param status Status atual da manutenção.
     * @return Lista de manutenções com o status informado.
     */
    List<ManutencaoAtivoResponse> buscarPorStatus(StatusManutencao status);

    /**
     * Lista todas as manutenções realizadas dentro de um intervalo de datas.
     *
     * @param inicio Data inicial.
     * @param fim    Data final.
     * @return Lista de manutenções realizadas no período informado.
     */
    List<ManutencaoAtivoResponse> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Remove uma manutenção do sistema.
     *
     * @param id ID da manutenção a ser removida.
     */
    void deletar(Long id);
}