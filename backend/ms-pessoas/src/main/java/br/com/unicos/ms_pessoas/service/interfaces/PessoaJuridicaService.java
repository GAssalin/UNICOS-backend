package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas às Pessoas Jurídicas
 * cadastradas no UniCoS.
 */
public interface PessoaJuridicaService {

    /**
     * Cria uma nova pessoa jurídica.
     */
    PessoaJuridicaResponse criar(PessoaJuridicaRequest request);

    /**
     * Atualiza uma pessoa jurídica existente.
     */
    PessoaJuridicaResponse atualizar(Long id, PessoaJuridicaRequest request);

    /**
     * Exclui uma pessoa jurídica pelo ID.
     */
    void excluir(Long id);

    /**
     * Busca pessoa jurídica pelo ID.
     */
    Optional<PessoaJuridicaResponse> buscarPorId(Long id);

    /**
     * Lista todas as pessoas jurídicas.
     */
    List<PessoaJuridicaListDTO> listarTodas();

    /**
     * Busca pessoa jurídica pelo CNPJ.
     */
    Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj);

    /**
     * Lista pessoa jurídica pelo nome fantasia (exato).
     */
    List<PessoaJuridicaListDTO> listarPorNomeFantasia(String nomeFantasia);

    /**
     * Lista pessoas jurídicas cujo nome contenha o texto informado.
     */
    List<PessoaJuridicaListDTO> listarPorNome(String nome);
}
