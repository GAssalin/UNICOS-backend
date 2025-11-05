package br.com.unicos.ms_ativos.service;

import br.com.unicos.ms_ativos.dto.DocumentoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.DocumentoAtivoRequest;
import br.com.unicos.ms_ativos.dto.DocumentoAtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface responsável pelas regras de negócio da entidade {@code DocumentoAtivo}.
 * <p>
 * Define os métodos de CRUD e as consultas específicas para o gerenciamento
 * de documentos vinculados a ativos (notas fiscais, garantias, laudos etc.).
 */
public interface DocumentoAtivoService {

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Cria e salva um novo documento vinculado a um ativo.
     *
     * @param request DTO contendo os dados do documento a ser criado.
     * @return DTO representando o documento criado.
     */
    @Transactional
    DocumentoAtivoResponse salvar(DocumentoAtivoRequest request);

    /**
     * Atualiza as informações de um documento existente.
     *
     * @param id      identificador do documento.
     * @param request DTO contendo os novos dados.
     * @return DTO atualizado do documento.
     */
    @Transactional
    DocumentoAtivoResponse atualizar(Long id, DocumentoAtivoRequest request);

    /**
     * Exclui um documento pelo seu identificador.
     *
     * @param id identificador do documento a ser removido.
     */
    @Transactional
    void excluir(Long id);

    /**
     * Busca um documento específico pelo seu ID.
     *
     * @param id identificador do documento.
     * @return DTO com os dados do documento, se encontrado.
     */
    Optional<DocumentoAtivoResponse> buscarPorId(Long id);

    /**
     * Retorna todos os documentos cadastrados.
     *
     * @return lista de documentos em formato resumido.
     */
    List<DocumentoAtivoListDTO> listarTodos();

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todos os documentos vinculados a um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de documentos associados ao ativo.
     */
    List<DocumentoAtivoListDTO> buscarPorAtivo(Long ativoId);

    /**
     * Retorna os documentos filtrados por tipo (nota fiscal, garantia, laudo, etc.).
     *
     * @param tipo tipo do documento.
     * @return lista de documentos do tipo informado.
     */
    List<DocumentoAtivoListDTO> buscarPorTipo(TipoDocumentoAtivo tipo);

    /**
     * Retorna os documentos filtrados por status (VÁLIDO, EXPIRADO, VENCIDO, etc.).
     *
     * @param status status atual do documento.
     * @return lista de documentos com o status informado.
     */
    List<DocumentoAtivoListDTO> buscarPorStatus(StatusDocumento status);

    /**
     * Retorna os documentos emitidos dentro de um intervalo de datas.
     *
     * @param inicio data inicial do intervalo.
     * @param fim    data final do intervalo.
     * @return lista de documentos emitidos no período.
     */
    List<DocumentoAtivoListDTO> buscarPorPeriodoEmissao(LocalDate inicio, LocalDate fim);

    /**
     * Busca o último documento do tipo "nota fiscal" vinculado a um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return documento mais recente do tipo "nota fiscal", se existir.
     */
    Optional<DocumentoAtivoResponse> buscarUltimaNotaFiscal(Long ativoId);

    /**
     * Retorna os documentos de garantia ainda válidos.
     *
     * @return lista de documentos com tipo GARANTIA e status VÁLIDO.
     */
    List<DocumentoAtivoListDTO> buscarGarantiasValidas();

    /**
     * Retorna os documentos que estão vencidos ou expirados.
     *
     * @return lista de documentos vencidos.
     */
    List<DocumentoAtivoListDTO> buscarDocumentosVencidos();
}
