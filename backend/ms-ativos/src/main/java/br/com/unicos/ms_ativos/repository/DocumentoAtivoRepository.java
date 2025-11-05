package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;
import br.com.unicos.ms_ativos.model.DocumentoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link DocumentoAtivo}.
 * <p>
 * Fornece consultas específicas para controle de documentos vinculados aos ativos,
 * permitindo filtragem por tipo, status, data e validade.
 */
@Repository
public interface DocumentoAtivoRepository extends JpaRepository<DocumentoAtivo, Long> {

    // ===========================================================
    // 🔍 CONSULTAS BÁSICAS
    // ===========================================================

    /**
     * Retorna todos os documentos vinculados a um ativo específico.
     *
     * @param ativoId identificador do ativo
     * @return lista de documentos do ativo
     */
    List<DocumentoAtivo> findByAtivoId(Long ativoId);

    /**
     * Busca documento pelo número e tipo.
     *
     * @param numero número do documento
     * @param tipo   tipo do documento (nota fiscal, garantia, etc.)
     * @return documento correspondente, se existir
     */
    Optional<DocumentoAtivo> findByNumeroAndTipo(String numero, TipoDocumentoAtivo tipo);

    /**
     * Verifica se já existe um documento do mesmo tipo vinculado ao ativo.
     *
     * @param ativoId identificador do ativo
     * @param tipo    tipo do documento
     * @return true se existir, false caso contrário
     */
    boolean existsByAtivoIdAndTipo(Long ativoId, TipoDocumentoAtivo tipo);

    /**
     * Retorna todos os documentos de um determinado tipo.
     *
     * @param tipo tipo de documento
     * @return lista de documentos do tipo informado
     */
    List<DocumentoAtivo> findByTipo(TipoDocumentoAtivo tipo);

    /**
     * Retorna todos os documentos com um status específico.
     *
     * @param status status do documento
     * @return lista de documentos com o status informado
     */
    List<DocumentoAtivo> findByStatus(StatusDocumento status);

    // ===========================================================
    // 📅 CONSULTAS POR DATA
    // ===========================================================

    /**
     * Retorna todos os documentos emitidos em uma data específica.
     *
     * @param dataEmissao data de emissão do documento
     * @return lista de documentos emitidos na data
     */
    List<DocumentoAtivo> findByDataEmissao(LocalDate dataEmissao);

    /**
     * Retorna documentos emitidos dentro de um intervalo de datas.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de documentos emitidos no período
     */
    List<DocumentoAtivo> findByDataEmissaoBetween(LocalDate inicio, LocalDate fim);

    // ===========================================================
    // 🧠 CONSULTAS CUSTOMIZADAS (JPQL)
    // ===========================================================

    /**
     * Consulta personalizada: busca documentos vencidos ou expirados.
     *
     * @return lista de documentos com status vencido ou expirado
     */
    @Query("SELECT d FROM DocumentoAtivo d WHERE d.status IN ('VENCIDO', 'EXPIRADO')")
    List<DocumentoAtivo> buscarDocumentosVencidos();

    /**
     * Consulta personalizada: busca documentos em revisão.
     *
     * @return lista de documentos em revisão
     */
    @Query("SELECT d FROM DocumentoAtivo d WHERE d.status = 'EM_REVISAO'")
    List<DocumentoAtivo> buscarDocumentosEmRevisao();

    /**
     * Consulta personalizada: retorna o último documento do tipo nota fiscal cadastrado para o ativo.
     *
     * @param ativoId identificador do ativo
     * @return último documento de nota fiscal do ativo, se existir
     */
    @Query("SELECT d FROM DocumentoAtivo d WHERE d.ativo.id = :ativoId AND d.tipo = 'NOTA_FISCAL' ORDER BY d.dataEmissao DESC LIMIT 1")
    Optional<DocumentoAtivo> buscarUltimaNotaFiscalPorAtivo(Long ativoId);

    /**
     * Consulta personalizada: busca documentos de garantia válidos.
     *
     * @return lista de documentos de garantia ainda válidos
     */
    @Query("SELECT d FROM DocumentoAtivo d WHERE d.tipo = 'GARANTIA' AND d.status = 'VALIDO'")
    List<DocumentoAtivo> buscarGarantiasValidas();

    /**
     * Consulta personalizada: busca documentos de laudos técnicos emitidos nos últimos 6 meses.
     *
     * @return lista de laudos técnicos recentes
     */
    @Query("SELECT d FROM DocumentoAtivo d WHERE d.tipo = 'LAUDO_TECNICO' AND d.dataEmissao >= CURRENT_DATE - 180")
    List<DocumentoAtivo> buscarLaudosRecentes();

    // ===========================================================
    // 📊 RELATÓRIOS E INDICADORES
    // ===========================================================

    /**
     * Retorna o total de documentos agrupados por tipo.
     *
     * @return lista contendo o tipo e a contagem de documentos
     */
    @Query("SELECT d.tipo, COUNT(d) FROM DocumentoAtivo d GROUP BY d.tipo")
    List<Object[]> contarDocumentosPorTipo();

    /**
     * Retorna o total de documentos agrupados por status.
     *
     * @return lista contendo o status e a contagem de documentos
     */
    @Query("SELECT d.status, COUNT(d) FROM DocumentoAtivo d GROUP BY d.status")
    List<Object[]> contarDocumentosPorStatus();

    /**
     * Consulta personalizada: retorna o número de documentos associados por ativo.
     *
     * @return lista de objetos com o ID do ativo e a contagem de documentos
     */
    @Query("SELECT d.ativo.id, COUNT(d) FROM DocumentoAtivo d GROUP BY d.ativo.id")
    List<Object[]> contarDocumentosPorAtivo();

    /**
     * Consulta personalizada: retorna os ativos com documentos vencidos.
     *
     * @return lista de IDs de ativos que possuem pelo menos um documento vencido
     */
    @Query("SELECT DISTINCT d.ativo.id FROM DocumentoAtivo d WHERE d.status IN ('VENCIDO', 'EXPIRADO')")
    List<Long> buscarAtivosComDocumentosVencidos();
}
