package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.EnderecoEmpresaListDTO;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaResponse;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.EnderecoEmpresa}.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * dos endereços vinculados a empresas e filiais.
 */
public interface EnderecoEmpresaService {

    /**
     * Cria e salva um novo endereço empresarial.
     *
     * @param request DTO contendo os dados do endereço.
     * @return DTO representando o endereço salvo.
     */
    EnderecoEmpresaResponse salvar(EnderecoEmpresaRequest request);

    /**
     * Atualiza os dados de um endereço empresarial existente.
     *
     * @param id      ID do endereço a ser atualizado.
     * @param request DTO contendo os novos dados.
     * @return DTO representando o endereço atualizado.
     */
    EnderecoEmpresaResponse atualizar(Long id, EnderecoEmpresaRequest request);

    /**
     * Busca um endereço pelo seu ID.
     *
     * @param id ID do endereço.
     * @return Optional contendo o endereço, se encontrado.
     */
    Optional<EnderecoEmpresaResponse> buscarPorId(Long id);

    /**
     * Lista todos os endereços vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de endereços da empresa.
     */
    List<EnderecoEmpresaListDTO> listarPorEmpresa(Long empresaId);

    /**
     * Lista todos os endereços vinculados a uma filial específica.
     *
     * @param filialId ID da filial.
     * @return Lista de endereços da filial.
     */
    List<EnderecoEmpresaListDTO> listarPorFilial(Long filialId);

    /**
     * Busca um endereço pelo CEP.
     *
     * @param cep Código postal do endereço.
     * @return Optional contendo o endereço, se encontrado.
     */
    Optional<EnderecoEmpresaResponse> buscarPorCep(String cep);

    /**
     * Lista todos os endereços localizados em uma cidade específica.
     *
     * @param cidade Nome da cidade.
     * @return Lista de endereços da cidade informada.
     */
    List<EnderecoEmpresaListDTO> listarPorCidade(String cidade);

    /**
     * Lista todos os endereços de uma determinada unidade federativa (estado).
     *
     * @param estado Sigla do estado (ex: SP, RJ, MG).
     * @return Lista de endereços do estado informado.
     */
    List<EnderecoEmpresaListDTO> listarPorEstado(String estado);

    /**
     * Lista todos os endereços de um tipo específico
     * (ex: MATRIZ, FATURAMENTO, ENTREGA).
     *
     * @param tipo Tipo de endereço.
     * @return Lista de endereços correspondentes ao tipo informado.
     */
    List<EnderecoEmpresaListDTO> listarPorTipo(TipoEnderecoEmpresa tipo);

    /**
     * Lista endereços filtrados por tipo e cidade.
     *
     * @param tipo   Tipo de endereço.
     * @param cidade Cidade do endereço.
     * @return Lista de endereços correspondentes.
     */
    List<EnderecoEmpresaListDTO> listarPorTipoECidade(TipoEnderecoEmpresa tipo, String cidade);

    /**
     * Lista endereços filtrados por tipo e estado.
     *
     * @param tipo   Tipo de endereço.
     * @param estado Estado do endereço.
     * @return Lista de endereços correspondentes.
     */
    List<EnderecoEmpresaListDTO> listarPorTipoEEstado(TipoEnderecoEmpresa tipo, String estado);

    /**
     * Lista todos os endereços de uma empresa ordenados
     * alfabeticamente por cidade.
     *
     * @param empresaId ID da empresa.
     * @return Lista de endereços ordenada por cidade.
     */
    List<EnderecoEmpresaListDTO> listarPorEmpresaOrdenados(Long empresaId);

    /**
     * Lista todos os endereços cadastrados,
     * ordenados por estado e cidade.
     *
     * @return Lista de endereços ordenada.
     */
    List<EnderecoEmpresaListDTO> listarOrdenadosPorEstadoECidade();

    /**
     * Remove um endereço empresarial do sistema.
     *
     * @param id ID do endereço a ser removido.
     */
    void deletar(Long id);
}
