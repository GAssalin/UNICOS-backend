package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.ManutencaoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.ManutencaoAtivoRequest;
import br.com.unicos.ms_ativos.dto.ManutencaoAtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code ManutencaoAtivo}.
 * <p>
 * Define os métodos de controle, agendamento e análise de manutenções preventivas
 * e corretivas realizadas nos ativos patrimoniais.
 */
public interface ManutencaoAtivoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra uma nova manutenção no sistema.
     *
     * @param request DTO contendo os dados da manutenção.
     * @return DTO representando a manutenção criada.
     */
    @Transactional
    ManutencaoAtivoResponse salvar(ManutencaoAtivoRequest request);

    /**
     * Atualiza as informações de uma manutenção existente.
     *
     * @param id      identificador da manutenção.
     * @param request DTO com os novos dados.
     * @return DTO atualizado com as informações persistidas.
     */
    @Transactional
    ManutencaoAtivoResponse atualizar(Long id, ManutencaoAtivoRequest request);

    /**
     * Exclui uma manutenção com base no seu ID.
     *
     * @param id identificador da manutenção.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca uma manutenção específica pelo seu ID.
     *
     * @param id identificador da manutenção.
     * @return DTO detalhado, se encontrado.
     */
    Optional<ManutencaoAtivoResponse> buscarPorId(Long id);

    /**
     * Lista todas as manutenções cadastradas.
     *
     * @return lista resumida de manutenções.
     */
    List<ManutencaoAtivoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as manutenções vinculadas a um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de manutenções do ativo.
     */
    List<ManutencaoAtivoListDTO> buscarPorAtivo(Long ativoId);

    /**
     * Retorna manutenções realizadas por um fornecedor específico.
     *
     * @param fornecedorId identificador do fornecedor.
     * @return lista de manutenções associadas ao fornecedor.
     */
    List<ManutencaoAtivoListDTO> buscarPorFornecedor(Long fornecedorId);

    /**
     * Retorna manutenções filtradas por tipo (PREVENTIVA ou CORRETIVA).
     *
     * @param tipo tipo da manutenção.
     * @return lista de manutenções do tipo informado.
     */
    List<ManutencaoAtivoListDTO> buscarPorTipo(TipoManutencao tipo);

    /**
     * Retorna manutenções filtradas por status (ABERTA, EM_EXECUCAO, CONCLUIDA etc.).
     *
     * @param status status atual da manutenção.
     * @return lista de manutenções com o status informado.
     */
    List<ManutencaoAtivoListDTO> buscarPorStatus(StatusManutencao status);

    /**
     * Retorna as manutenções realizadas dentro de um período específico.
     *
     * @param inicio data inicial do intervalo.
     * @param fim    data final do intervalo.
     * @return lista de manutenções no período informado.
     */
    List<ManutencaoAtivoListDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Retorna manutenções agendadas para uma data específica.
     *
     * @param data data de referência.
     * @return lista de manutenções agendadas.
     */
    List<ManutencaoAtivoListDTO> buscarAgendadasPara(LocalDate data);

    // ===========================================================
    // 💰 RELATÓRIOS E ANÁLISES
    // ===========================================================

    /**
     * Calcula o custo total de manutenções de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return valor total gasto em manutenções.
     */
    BigDecimal calcularCustoTotalPorAtivo(Long ativoId);

    /**
     * Calcula o custo total de manutenções realizadas em um período.
     *
     * @param inicio data inicial.
     * @param fim    data final.
     * @return valor total gasto no período.
     */
    BigDecimal calcularCustoTotalPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Retorna o número total de manutenções registradas.
     *
     * @return quantidade total de manutenções.
     */
    Long contarTotal();

    /**
     * Retorna a quantidade de manutenções por tipo (PREVENTIVA, CORRETIVA etc.).
     *
     * @return lista de contagens agrupadas por tipo.
     */
    List<Object[]> contarPorTipo();

    /**
     * Retorna a quantidade de manutenções por status (ABERTA, CONCLUÍDA etc.).
     *
     * @return lista de contagens agrupadas por status.
     */
    List<Object[]> contarPorStatus();

    /**
     * Retorna os ativos que possuem mais de uma manutenção registrada.
     *
     * @return lista de ativos com alto índice de manutenção.
     */
    List<Object[]> buscarAtivosComManutencoesFrequentes();

    /**
     * Retorna as manutenções mais recentes registradas no sistema.
     *
     * @param limite quantidade máxima de registros a retornar.
     * @return lista das manutenções mais recentes.
     */
    List<ManutencaoAtivoListDTO> buscarRecentes(int limite);
}
