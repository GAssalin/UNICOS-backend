package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.TransferenciaAtivoListDTO;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoRequest;
import br.com.unicos.ms_ativos.dto.TransferenciaAtivoResponse;
import br.com.unicos.ms_ativos.enums.TipoTransferencia;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code TransferenciaAtivo}.
 * <p>
 * Define os métodos para registro, consulta e análise das transferências
 * de ativos entre unidades, setores e filiais.
 */
public interface TransferenciaAtivoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra uma nova transferência de ativo.
     *
     * @param request DTO contendo os dados da transferência.
     * @return DTO representando a transferência criada.
     */
    @Transactional
    TransferenciaAtivoResponse salvar(TransferenciaAtivoRequest request);

    /**
     * Atualiza as informações de uma transferência existente.
     *
     * @param id      identificador da transferência.
     * @param request DTO com os novos dados.
     * @return DTO atualizado da transferência.
     */
    @Transactional
    TransferenciaAtivoResponse atualizar(Long id, TransferenciaAtivoRequest request);

    /**
     * Exclui uma transferência com base no seu ID.
     *
     * @param id identificador da transferência.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca uma transferência específica pelo seu ID.
     *
     * @param id identificador da transferência.
     * @return DTO detalhado, se encontrado.
     */
    Optional<TransferenciaAtivoResponse> buscarPorId(Long id);

    /**
     * Retorna todas as transferências registradas no sistema.
     *
     * @return lista resumida de transferências.
     */
    List<TransferenciaAtivoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as transferências realizadas para um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de transferências do ativo.
     */
    List<TransferenciaAtivoListDTO> buscarPorAtivo(Long ativoId);

    /**
     * Retorna as transferências originadas de uma determinada filial/unidade.
     *
     * @param origemId identificador da unidade de origem.
     * @return lista de transferências originadas na unidade.
     */
    List<TransferenciaAtivoListDTO> buscarPorOrigem(Long origemId);

    /**
     * Retorna as transferências destinadas a uma determinada filial/unidade.
     *
     * @param destinoId identificador da unidade de destino.
     * @return lista de transferências destinadas à unidade.
     */
    List<TransferenciaAtivoListDTO> buscarPorDestino(Long destinoId);

    /**
     * Retorna as transferências realizadas dentro de um período específico.
     *
     * @param inicio data inicial do intervalo.
     * @param fim    data final do intervalo.
     * @return lista de transferências no período informado.
     */
    List<TransferenciaAtivoListDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Retorna as transferências realizadas por um usuário específico.
     *
     * @param responsavelId identificador do usuário responsável.
     * @return lista de transferências realizadas pelo usuário.
     */
    List<TransferenciaAtivoListDTO> buscarPorResponsavel(Long responsavelId);

    /**
     * Retorna as transferências de um determinado tipo (INTERNA, ENTRE_FILIAIS, BAIXA, OUTROS).
     *
     * @param tipo tipo de transferência.
     * @return lista de transferências do tipo informado.
     */
    List<TransferenciaAtivoListDTO> buscarPorTipo(TipoTransferencia tipo);

    // ===========================================================
    // 📊 RELATÓRIOS E RASTREABILIDADE
    // ===========================================================

    /**
     * Retorna o histórico completo de movimentações de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return lista cronológica de transferências do ativo.
     */
    List<TransferenciaAtivoListDTO> buscarHistoricoDeMovimentacoes(Long ativoId);

    /**
     * Retorna o número total de transferências registradas.
     *
     * @return quantidade total de registros de transferência.
     */
    Long contarTotal();

    /**
     * Retorna o número de transferências realizadas por tipo.
     *
     * @return lista de contagens agrupadas por tipo de transferência.
     */
    List<Object[]> contarPorTipo();

    /**
     * Retorna o número de transferências realizadas por unidade de origem.
     *
     * @return lista de contagens agrupadas por unidade de origem.
     */
    List<Object[]> contarPorOrigem();

    /**
     * Retorna o número de transferências realizadas por unidade de destino.
     *
     * @return lista de contagens agrupadas por unidade de destino.
     */
    List<Object[]> contarPorDestino();

    /**
     * Retorna as transferências mais recentes registradas no sistema.
     *
     * @param limite quantidade máxima de registros.
     * @return lista das transferências mais recentes.
     */
    List<TransferenciaAtivoListDTO> buscarRecentes(int limite);
}
