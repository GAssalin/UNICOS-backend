package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de municípios dentro do UniCoS.
 * Usado para cadastros, validações e integrações baseadas em localização.
 */
public interface MunicipioService {

    /**
     * Cria um novo município.
     */
    MunicipioResponse criar(MunicipioRequest request);

    /**
     * Atualiza um município existente.
     */
    MunicipioResponse atualizar(Long id, MunicipioRequest request);

    /**
     * Remove um município pelo ID.
     */
    void excluir(Long id);

    /**
     * Busca município por ID.
     */
    Optional<MunicipioResponse> buscarPorId(Long id);

    /**
     * Lista todos os municípios cadastrados.
     */
    List<MunicipioListDTO> listarTodos();

    /**
     * Lista municípios cujo nome contenha o texto informado.
     */
    List<MunicipioListDTO> listarPorNome(String nome);

    /**
     * Lista municípios filtrados por UF.
     */
    List<MunicipioListDTO> listarPorUf(String uf);

    /**
     * Busca um município pelo código IBGE.
     */
    Optional<MunicipioResponse> buscarPorCodigoIbge(String codigoIbge);
}
