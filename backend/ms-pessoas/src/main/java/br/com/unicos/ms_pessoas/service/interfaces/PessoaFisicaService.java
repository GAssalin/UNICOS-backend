package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas às Pessoas Físicas
 * cadastradas no UniCoS.
 */
public interface PessoaFisicaService {

    /**
     * Cria uma nova pessoa física.
     */
    PessoaFisicaResponse criar(PessoaFisicaRequest request);

    /**
     * Atualiza uma pessoa física existente.
     */
    PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request);

    /**
     * Exclui uma pessoa física pelo ID.
     */
    void excluir(Long id);

    /**
     * Busca pessoa física pelo ID.
     */
    Optional<PessoaFisicaResponse> buscarPorId(Long id);

    /**
     * Lista todas as pessoas físicas.
     */
    List<PessoaFisicaListDTO> listarTodas();

    /**
     * Busca pessoa física pelo CPF.
     */
    Optional<PessoaFisicaResponse> buscarPorCpf(String cpf);

    /**
     * Lista pessoas físicas pelo nome social.
     */
    List<PessoaFisicaListDTO> listarPorNomeSocial(String nomeSocial);

    /**
     * Lista pessoas físicas cujo nome contenha o texto informado.
     */
    List<PessoaFisicaListDTO> listarPorNome(String nome);
}
