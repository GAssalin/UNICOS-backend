package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoRequest;
import br.com.unicos.ms_ativos.dto.DepreciacaoAtivoResponse;
import br.com.unicos.ms_ativos.enums.TipoDepreciacao;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code DepreciacaoAtivo}.
 * <p>
 * Define os métodos de cálculo, controle e consulta de depreciações contábeis
 * aplicadas aos ativos patrimoniais.
 */
public interface DepreciacaoAtivoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Cria e registra uma nova depreciação para o ativo informado.
     *
     * @param request DTO contendo os dados da depreciação.
     * @return DTO com as informações da depreciação criada.
     */
    @Transactional
    DepreciacaoAtivoResponse salvar(DepreciacaoAtivoRequest request);

    /**
     * Atualiza os dados de uma depreciação existente.
     *
     * @param id      identificador da depreciação.
     * @param request DTO contendo os novos valores.
     * @return DTO atualizado da depreciação.
     */
    @Transactional
    DepreciacaoAtivoResponse atualizar(Long id, DepreciacaoAtivoRequest request);

    /**
     * Exclui uma depreciação pelo seu identificador.
     *
     * @param id identificador da depreciação a ser excluída.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca uma depreciação específica pelo ID.
     *
     * @param id identificador da depreciação.
     * @return DTO detalhado, se encontrada.
     */
    Optional<DepreciacaoAtivoResponse> buscarPorId(Long id);

    /**
     * Lista todas as depreciações registradas no sistema.
     *
     * @return lista resumida de depreciações.
     */
    List<DepreciacaoAtivoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as depreciações vinculadas a um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return lista de depreciações do ativo.
     */
    List<DepreciacaoAtivoListDTO> buscarPorAtivo(Long ativoId);

    /**
     * Retorna as depreciações filtradas por tipo (LINEAR, ACELERADA, REAVALIAÇÃO, etc.).
     *
     * @param tipo tipo de depreciação.
     * @return lista de depreciações do tipo informado.
     */
    List<DepreciacaoAtivoListDTO> buscarPorTipo(TipoDepreciacao tipo);

    /**
     * Retorna as depreciações realizadas em um período específico.
     *
     * @param inicio data inicial do intervalo.
     * @param fim    data final do intervalo.
     * @return lista de depreciações no período informado.
     */
    List<DepreciacaoAtivoListDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Busca a depreciação de um ativo em uma data de competência específica.
     *
     * @param ativoId         identificador do ativo.
     * @param dataCompetencia data da competência contábil.
     * @return depreciação correspondente, se existir.
     */
    Optional<DepreciacaoAtivoResponse> buscarPorCompetencia(Long ativoId, LocalDate dataCompetencia);

    // ===========================================================
    // 💰 CONSULTAS CONTÁBEIS E RELATÓRIOS
    // ===========================================================

    /**
     * Calcula o valor total depreciado de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return soma total dos valores depreciados.
     */
    BigDecimal calcularValorTotalDepreciado(Long ativoId);

    /**
     * Retorna o saldo contábil mais recente do ativo.
     *
     * @param ativoId identificador do ativo.
     * @return saldo contábil atualizado.
     */
    Optional<BigDecimal> buscarSaldoContabilAtual(Long ativoId);

    /**
     * Calcula o valor médio de depreciação mensal de um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return valor médio de depreciação mensal.
     */
    BigDecimal calcularMediaDepreciacaoMensal(Long ativoId);

    /**
     * Retorna as depreciações realizadas no mês atual.
     *
     * @return lista de depreciações do mês corrente.
     */
    List<DepreciacaoAtivoListDTO> buscarDepreciacoesDoMesAtual();
}
