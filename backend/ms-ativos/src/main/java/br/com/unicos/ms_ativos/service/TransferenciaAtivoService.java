package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.TransferenciaAtivoRequest;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade TransferenciaAtivo.
 *
 * Define os métodos de criação, atualização, listagem e exclusão
 * das transferências de ativos cadastradas no sistema.
 */
public interface TransferenciaAtivoService {

    /**
     * Salva uma nova transferência de ativo.
     *
     * @param request DTO contendo os dados da transferência.
     * @return DTO representando a transferência salva.
     */
    TransferenciaAtivoResponse salvar(TransferenciaAtivoRequest request);

    /**
     * Atualiza os dados de uma transferência existente.
     *
     * @param id ID da transferência a ser atualizada.
     * @param request DTO contendo os novos dados da transferência.
     * @return DTO representando a transferência atualizada.
     */
    TransferenciaAtivoResponse atualizar(Long id, TransferenciaAtivoRequest request);

    /**
     * Busca uma transferência pelo ID.
     *
     * @param id ID da transferência.
     * @return Optional contendo o DTO da transferência, se encontrada.
     */
    Optional<TransferenciaAtivoResponse> buscarPorId(Long id);

    /**
     * Lista todas as transferências cadastradas.
     *
     * @return Lista de transferências.
     */
    List<TransferenciaAtivoResponse> listarTodas();

    /**
     * Lista todas as transferências de um ativo específico.
     *
     * @param ativoId ID do ativo transferido.
     * @return Lista de transferências associadas ao ativo.
     */
    List<TransferenciaAtivoResponse> buscarPorAtivo(Long ativoId);

    /**
     * Lista as transferências realizadas a partir de uma filial específica.
     *
     * @param origemId ID da filial de origem.
     * @return Lista de transferências originadas da filial.
     */
    List<TransferenciaAtivoResponse> buscarPorOrigem(Long origemId);

    /**
     * Lista as transferências destinadas a uma filial específica.
     *
     * @param destinoId ID da filial de destino.
     * @return Lista de transferências destinadas à filial.
     */
    List<TransferenciaAtivoResponse> buscarPorDestino(Long destinoId);

    /**
     * Lista todas as transferências realizadas dentro de um intervalo de datas.
     *
     * @param inicio Data inicial.
     * @param fim Data final.
     * @return Lista de transferências realizadas no período informado.
     */
    List<TransferenciaAtivoResponse> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Remove uma transferência do sistema.
     *
     * @param id ID da transferência a ser removida.
     */
    void deletar(Long id);
}