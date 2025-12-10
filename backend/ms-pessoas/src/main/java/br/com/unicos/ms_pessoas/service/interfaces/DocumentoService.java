package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos documentos
 * vinculados às pessoas dentro do UniCoS.
 *
 * <p>
 * Documentos incluem CPF, RG, CNPJ e outros registros formais utilizados
 * em validações legais e integrações corporativas.
 * </p>
 */
public interface DocumentoService {

    /**
     * Cria um novo documento para uma pessoa.
     *
     * @param request dados do documento.
     * @return informações detalhadas do documento criado.
     */
    DocumentoResponse criar(DocumentoRequest request);

    /**
     * Atualiza um documento existente.
     *
     * @param id      identificador do documento.
     * @param request dados atualizados.
     * @return informações detalhadas do documento.
     */
    DocumentoResponse atualizar(Long id, DocumentoRequest request);

    /**
     * Remove um documento pelo ID.
     *
     * @param id identificador do documento.
     */
    void excluir(Long id);

    /**
     * Busca documento pelo ID.
     *
     * @param id identificador do documento.
     * @return documento encontrado, se existir.
     */
    Optional<DocumentoResponse> buscarPorId(Long id);

    /**
     * Lista todos os documentos cadastrados.
     *
     * @return lista simplificada.
     */
    List<DocumentoListDTO> listarTodos();

    /**
     * Lista todos os documentos pertencentes a uma pessoa.
     *
     * @param pessoaId ID da pessoa proprietária.
     * @return lista de documentos.
     */
    List<DocumentoListDTO> listarPorPessoa(Long pessoaId);

    /**
     * Lista documentos de um tipo específico.
     *
     * @param tipo tipo do documento.
     * @return lista de documentos.
     */
    List<DocumentoListDTO> listarPorTipo(String tipo);
}
