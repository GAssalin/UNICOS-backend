package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.Uf;
import br.com.unicos.ms_pessoas.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Municipio}.
 * <p>
 * Oferece consultas utilizadas em cadastros de endereços, validações de CEP,
 * integrações geográficas e demais operações que dependem da localização.
 */
@Repository
public interface MunicipioRepository extends BaseTenantRepository<Municipio, Long> {

    /**
     * Busca um município pelo nome exato.
     *
     * @param nome Nome do município.
     * @return Município correspondente, caso exista.
     */
    Optional<Municipio> findByNome(String nome);

    /**
     * Lista municípios cujo nome contenha o termo informado,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome Parte do nome.
     * @return Lista de municípios encontrados.
     */
    List<Municipio> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista municípios pertencentes a uma determinada unidade federativa.
     *
     * @param uf Unidade federativa (UF).
     * @return Lista de municípios da UF informada.
     */
    List<Municipio> findByUf(Uf uf);

    /**
     * Busca um município pelo seu código IBGE.
     *
     * @param codigoIbge Código IBGE.
     * @return Município correspondente, caso exista.
     */
    Optional<Municipio> findByCodigoIbge(String codigoIbge);
}
