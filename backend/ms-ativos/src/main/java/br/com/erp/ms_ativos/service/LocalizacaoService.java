package br.com.erp.ms_ativos.service;

import br.com.erp.ms_ativos.dto.LocalizacaoRequest;
import br.com.erp.ms_ativos.dto.LocalizacaoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade Localizacao.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * das localizações cadastradas no sistema.
 */
public interface LocalizacaoService {

    /**
     * Salva uma nova localização.
     *
     * @param request DTO contendo os dados da localização.
     * @return DTO representando a localização salva.
     */
    LocalizacaoResponse salvar(LocalizacaoRequest request);

    /**
     * Atualiza os dados de uma localização existente.
     *
     * @param id      ID da localização a ser atualizada.
     * @param request DTO contendo os novos dados da localização.
     * @return DTO representando a localização atualizada.
     */
    LocalizacaoResponse atualizar(Long id, LocalizacaoRequest request);

    /**
     * Busca uma localização pelo ID.
     *
     * @param id ID da localização.
     * @return Optional contendo o DTO da localização, se encontrada.
     */
    Optional<LocalizacaoResponse> buscarPorId(Long id);

    /**
     * Lista todas as localizações cadastradas.
     *
     * @return Lista de localizações.
     */
    List<LocalizacaoResponse> listarTodas();

    /**
     * Lista todas as localizações vinculadas a uma filial.
     *
     * @param filialId ID da filial.
     * @return Lista de localizações vinculadas à filial.
     */
    List<LocalizacaoResponse> buscarPorFilial(Long filialId);

    /**
     * Remove uma localização do sistema.
     *
     * @param id ID da localização a ser removida.
     */
    void deletar(Long id);
}