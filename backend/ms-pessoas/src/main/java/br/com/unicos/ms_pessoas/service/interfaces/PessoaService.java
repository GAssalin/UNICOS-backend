package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por consultas e operações gerais relacionadas à entidade {@link Pessoa}.
 * <p>
 * Como Pessoa é uma classe abstrata, operações de criação e atualização são tratadas
 * nos serviços específicos de Pessoa Física e Pessoa Jurídica. Este serviço lida com
 * listagens, buscas e filtros independentes do tipo.
 */
public interface PessoaService {

    /**
     * Busca uma pessoa pelo seu identificador.
     *
     * @param id identificador da pessoa.
     * @return DTO detalhado da pessoa, se encontrada.
     */
    Optional<PessoaResponse> buscarPorId(Long id);

    /**
     * Lista todas as pessoas cadastradas, independentemente do tipo (Física/Jurídica).
     *
     * @return lista simplificada de pessoas.
     */
    List<PessoaListDTO> listarTodas();

    /**
     * Lista pessoas cujo nome contenha o texto informado,
     * ignorando diferenciação de maiúsculas/minúsculas.
     *
     * @param nome parte do nome da pessoa.
     * @return lista de pessoas encontradas.
     */
    List<PessoaListDTO> listarPorNome(String nome);

    /**
     * Lista pessoas pelo nome exato.
     *
     * @param nome nome completo da pessoa.
     * @return lista de pessoas com o nome informado.
     */
    List<PessoaListDTO> listarPorNomeExato(String nome);

    /**
     * Lista pessoas filtradas por tipo.
     *
     * @param tipoPessoa tipo da pessoa (Física/Jurídica).
     * @return lista de pessoas do tipo informado.
     */
    List<PessoaListDTO> listarPorTipo(String tipoPessoa);
}
