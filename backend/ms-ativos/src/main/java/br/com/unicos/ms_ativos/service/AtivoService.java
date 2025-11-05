package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.AtivoListDTO;
import br.com.unicos.ms_ativos.dto.AtivoRequest;
import br.com.unicos.ms_ativos.dto.AtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code Ativo}.
 * <p>
 * Define os métodos principais de CRUD e consultas específicas relacionadas
 * à gestão de bens patrimoniais do sistema.
 */
public interface AtivoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Cria e salva um novo ativo no sistema.
     *
     * @param request DTO contendo os dados para criação do ativo.
     * @return DTO com os dados do ativo criado.
     */
    @Transactional
    AtivoResponse salvar(AtivoRequest request);

    /**
     * Atualiza as informações de um ativo existente.
     *
     * @param id      identificador do ativo a ser atualizado.
     * @param request DTO contendo os novos dados.
     * @return DTO com os dados atualizados do ativo.
     */
    @Transactional
    AtivoResponse atualizar(Long id, AtivoRequest request);

    /**
     * Remove um ativo com base no ID informado.
     *
     * @param id identificador do ativo.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca um ativo específico pelo seu ID.
     *
     * @param id identificador do ativo.
     * @return DTO com os dados detalhados do ativo.
     */
    Optional<AtivoResponse> buscarPorId(Long id);

    /**
     * Lista todos os ativos cadastrados no sistema.
     *
     * @return lista de ativos em formato resumido.
     */
    List<AtivoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Busca ativos filtrando por tipo (MÓVEL, IMÓVEL, VEÍCULO, etc.).
     *
     * @param tipo tipo de ativo.
     * @return lista de ativos do tipo informado.
     */
    List<AtivoListDTO> buscarPorTipo(TipoAtivo tipo);

    /**
     * Busca ativos filtrando por status (ATIVO, INATIVO, EM_MANUTENCAO, etc.).
     *
     * @param status status atual do ativo.
     * @return lista de ativos com o status informado.
     */
    List<AtivoListDTO> buscarPorStatus(StatusAtivo status);

    /**
     * Busca todos os ativos pertencentes a uma empresa específica.
     *
     * @param empresaId identificador da empresa.
     * @return lista de ativos da empresa.
     */
    List<AtivoListDTO> buscarPorEmpresa(Long empresaId);

    /**
     * Busca todos os ativos pertencentes a uma filial específica.
     *
     * @param filialId identificador da filial.
     * @return lista de ativos da filial.
     */
    List<AtivoListDTO> buscarPorFilial(Long filialId);

    /**
     * Busca ativos sob responsabilidade de um colaborador específico.
     *
     * @param responsavelId identificador do colaborador responsável.
     * @return lista de ativos associados ao responsável.
     */
    List<AtivoListDTO> buscarPorResponsavel(Long responsavelId);
}
