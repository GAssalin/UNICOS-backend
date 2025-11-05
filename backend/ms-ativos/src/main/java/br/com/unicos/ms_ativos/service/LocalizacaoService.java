package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.LocalizacaoListDTO;
import br.com.unicos.ms_ativos.dto.LocalizacaoRequest;
import br.com.unicos.ms_ativos.dto.LocalizacaoResponse;
import br.com.unicos.ms_ativos.model.HistoricoAtivo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code Localizacao}.
 * <p>
 * Define os métodos de CRUD e as consultas voltadas à organização física dos
 * ativos dentro das unidades (filiais) da empresa.
 */
public interface LocalizacaoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Cria e salva uma nova localização física dentro de uma filial.
     *
     * @param request DTO contendo os dados da localização.
     * @return DTO representando a localização criada.
     */
    @Transactional
    LocalizacaoResponse salvar(LocalizacaoRequest request);

    /**
     * Atualiza as informações de uma localização existente.
     *
     * @param id      identificador da localização.
     * @param request DTO com os novos dados.
     * @return DTO atualizado com as informações persistidas.
     */
    @Transactional
    LocalizacaoResponse atualizar(Long id, LocalizacaoRequest request);

    /**
     * Exclui uma localização com base em seu ID.
     *
     * @param id identificador da localização.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca uma localização específica pelo seu ID.
     *
     * @param id identificador da localização.
     * @return DTO detalhado, se encontrado.
     */
    Optional<LocalizacaoResponse> buscarPorId(Long id);

    /**
     * Retorna todas as localizações cadastradas no sistema.
     *
     * @return lista resumida de localizações.
     */
    List<LocalizacaoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todas as localizações vinculadas a uma filial específica.
     *
     * @param filialId identificador da filial.
     * @return lista de localizações da filial.
     */
    List<LocalizacaoListDTO> buscarPorFilial(Long filialId);

    /**
     * Busca localizações que contenham a descrição informada (ex: “Depósito”, “Sala”).
     *
     * @param descricao parte ou descrição completa da localização.
     * @return lista de localizações que correspondem ao filtro.
     */
    List<LocalizacaoListDTO> buscarPorDescricao(String descricao);

    /**
     * Verifica se já existe uma localização cadastrada com a mesma descrição em uma filial.
     *
     * @param descricao descrição da localização.
     * @param filialId  identificador da filial.
     * @return true se já existir, false caso contrário.
     */
    boolean existePorDescricaoEFilial(String descricao, Long filialId);

    /**
     * Retorna todas as localizações que possuem mais de um ativo vinculado.
     *
     * @return lista de localizações com múltiplos ativos associados.
     */
    List<LocalizacaoListDTO> buscarComMaisDeUmAtivo();

    /**
     * Retorna eventos recentes (últimos 7 dias) para auditoria e acompanhamento.
     *
     * @return lista de eventos recentes
     */
    @Query("SELECT h FROM HistoricoAtivo h WHERE h.dataEvento >= CURRENT_TIMESTAMP - 7 ORDER BY h.dataEvento DESC")
    List<HistoricoAtivo> buscarEventosRecentes();

    // ===========================================================
    // 📊 RELATÓRIOS E ORGANIZAÇÃO
    // ===========================================================

    /**
     * Retorna o total de localizações cadastradas em uma filial.
     *
     * @param filialId identificador da filial.
     * @return número total de localizações da filial.
     */
    Long contarPorFilial(Long filialId);

    /**
     * Retorna localizações que atualmente não possuem ativos associados.
     *
     * @return lista de localizações sem ativos vinculados.
     */
    List<LocalizacaoListDTO> buscarSemAtivos();

    /**
     * Retorna localizações que possuem ativos vinculados.
     *
     * @return lista de localizações com ativos associados.
     */
    List<LocalizacaoListDTO> buscarComAtivos();

    /**
     * Retorna o número total de localizações cadastradas no sistema.
     *
     * @return quantidade total de localizações.
     */
    Long contarTotal();
}
