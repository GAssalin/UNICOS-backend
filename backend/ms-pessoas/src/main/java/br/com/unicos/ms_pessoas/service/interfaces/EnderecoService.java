package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de endereços vinculados a pessoas no UniCoS.
 */
public interface EnderecoService {

    /**
     * Cria um novo endereço vinculado a uma pessoa.
     *
     * @param request dados do endereço.
     * @return endereço criado.
     */
    EnderecoResponse criar(EnderecoRequest request);

    /**
     * Atualiza um endereço existente.
     *
     * @param id      identificador do endereço.
     * @param request dados atualizados.
     * @return endereço atualizado.
     */
    EnderecoResponse atualizar(Long id, EnderecoRequest request);

    /**
     * Exclui um endereço pelo ID.
     *
     * @param id identificador.
     */
    void excluir(Long id);

    /**
     * Busca um endereço pelo ID.
     */
    Optional<EnderecoResponse> buscarPorId(Long id);

    /**
     * Lista todos os endereços do sistema.
     */
    List<EnderecoListDTO> listarTodos();

    /**
     * Lista endereços associados a uma pessoa.
     */
    List<EnderecoListDTO> listarPorPessoa(Long pessoaId);

    /**
     * Lista endereços de uma pessoa filtrados por tipo.
     */
    List<EnderecoListDTO> listarPorPessoaETipo(Long pessoaId, String tipo);

    /**
     * Lista endereços vinculados a um município.
     */
    List<EnderecoListDTO> listarPorMunicipio(Long municipioId);

    /**
     * Lista endereços filtrados por CEP.
     */
    List<EnderecoListDTO> listarPorCep(String cep);

    /**
     * Obtém o endereço principal de uma pessoa.
     */
    Optional<EnderecoResponse> buscarPrincipal(Long pessoaId);
}
