package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento dos tipos de relação entre pessoas
 * cadastrados no UniCoS.
 *
 * <p>Inclui operações de criação, atualização, exclusão e consultas diversas
 * utilizadas para vínculos como pai, mãe, dependente, sócio, representante
 * legal, tutor, entre outros.</p>
 */
public interface TipoRelacaoPessoaService {

    /**
     * Cria um novo tipo de relação.
     *
     * @param request dados necessários para criação.
     * @return DTO contendo os dados do tipo criado.
     */
    TipoRelacaoPessoaResponse criar(TipoRelacaoPessoaRequest request);

    /**
     * Atualiza um tipo de relação existente.
     *
     * @param id      identificador do tipo de relação.
     * @param request novos dados a serem aplicados.
     * @return DTO contendo os dados atualizados.
     */
    TipoRelacaoPessoaResponse atualizar(Long id, TipoRelacaoPessoaRequest request);

    /**
     * Exclui um tipo de relação.
     *
     * @param id identificador do tipo a ser excluído.
     */
    void excluir(Long id);

    /**
     * Busca um tipo de relação pelo ID.
     *
     * @param id identificador do tipo de relação.
     * @return DTO detalhado, se encontrado.
     */
    Optional<TipoRelacaoPessoaResponse> buscarPorId(Long id);

    /**
     * Lista todos os tipos de relação existentes.
     *
     * @return lista simplificada de tipos de relação.
     */
    List<TipoRelacaoPessoaListDTO> listarTodos();

    /**
     * Lista tipos de relação filtrados por nome parcial (contains, ignore case).
     *
     * @param nome parte do nome a ser filtrado.
     * @return lista de tipos encontrados.
     */
    List<TipoRelacaoPessoaListDTO> listarPorNome(String nome);

    /**
     * Busca tipo de relação pelo nome exato.
     *
     * @param nome nome completo do tipo de relação.
     * @return DTO detalhado, se encontrado.
     */
    Optional<TipoRelacaoPessoaResponse> buscarPorNomeExato(String nome);
}
