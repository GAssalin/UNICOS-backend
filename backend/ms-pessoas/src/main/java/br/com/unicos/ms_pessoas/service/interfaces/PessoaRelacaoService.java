package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de vínculos entre pessoas no UniCoS,
 * como dependentes, responsáveis legais, sócios e demais relações
 * corporativas.
 */
public interface PessoaRelacaoService {

    /**
     * Cria uma nova relação entre duas pessoas.
     *
     * @param request dados da relação a ser criada, incluindo IDs da pessoa principal,
     *                da pessoa relacionada e do tipo de relação.
     * @return DTO contendo os dados detalhados da relação criada.
     */
    PessoaRelacaoResponse criar(PessoaRelacaoRequest request);

    /**
     * Atualiza uma relação existente entre duas pessoas.
     *
     * @param id      identificador da relação a ser atualizada.
     * @param request novos dados da relação, incluindo IDs de pessoa, relacionado
     *                e tipo de relação.
     * @return DTO contendo os dados atualizados da relação.
     */
    PessoaRelacaoResponse atualizar(Long id, PessoaRelacaoRequest request);

    /**
     * Exclui uma relação pelo seu identificador.
     *
     * @param id identificador da relação a ser removida.
     */
    void excluir(Long id);

    /**
     * Busca uma relação pelo seu identificador.
     *
     * @param id identificador da relação.
     * @return DTO detalhado da relação, se encontrada.
     */
    Optional<PessoaRelacaoResponse> buscarPorId(Long id);

    /**
     * Lista todas as relações cadastradas no sistema.
     *
     * @return lista simplificada de relações.
     */
    List<PessoaRelacaoListDTO> listarTodas();

    /**
     * Lista todas as relações em que a pessoa indicada é o ator principal.
     *
     * @param pessoaId identificador da pessoa principal.
     * @return lista de relações em que a pessoa é o ator principal.
     */
    List<PessoaRelacaoListDTO> listarPorPessoa(Long pessoaId);

    /**
     * Lista todas as relações em que a pessoa indicada é o indivíduo relacionado.
     *
     * @param relacionadoId identificador da pessoa relacionada.
     * @return lista de relações em que a pessoa é o relacionado.
     */
    List<PessoaRelacaoListDTO> listarPorRelacionado(Long relacionadoId);

    /**
     * Lista relações filtradas por tipo de vínculo.
     *
     * @param tipoRelacaoPessoaId identificador do tipo de relação (por exemplo:
     *                             Pai, Mãe, Sócio, Responsável Legal).
     * @return lista de relações do tipo informado.
     */
    List<PessoaRelacaoListDTO> listarPorTipo(Long tipoRelacaoPessoaId);

    /**
     * Lista relações filtrando pelo nome da pessoa principal,
     * utilizando busca parcial (contains, ignore case).
     *
     * @param nome parte do nome da pessoa principal.
     * @return lista de relações encontradas.
     */
    List<PessoaRelacaoListDTO> listarPorPessoaENome(String nome);

    /**
     * Lista relações filtrando pelo nome da pessoa relacionada,
     * utilizando busca parcial (contains, ignore case).
     *
     * @param nome parte do nome da pessoa relacionada.
     * @return lista de relações encontradas.
     */
    List<PessoaRelacaoListDTO> listarPorRelacionadoENome(String nome);

    /**
     * Lista relações entre duas pessoas específicas, considerando
     * a combinação de pessoa principal e pessoa relacionada.
     *
     * @param pessoaId      identificador da pessoa principal.
     * @param relacionadoId identificador da pessoa relacionada.
     * @return lista de vínculos entre as duas pessoas.
     */
    List<PessoaRelacaoListDTO> listarPorPessoaERelacionado(Long pessoaId, Long relacionadoId);
}
