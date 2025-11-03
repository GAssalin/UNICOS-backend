package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.ConfiguracaoFiscal;
import br.com.unicos.ms_empresa.enums.TipoAmbiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade ConfiguracaoEmpresa.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface ConfiguracaoFiscalRepository extends JpaRepository<ConfiguracaoFiscal, Long> {

    /**
     * Busca a configuração fiscal e administrativa de uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Optional contendo a configuração da empresa, se encontrada.
     */
    Optional<ConfiguracaoFiscal> findByEmpresaId(Long empresaId);

    /**
     * Lista todas as configurações que utilizam um tipo de ambiente fiscal específico.
     *
     * @param tipoAmbiente Tipo de ambiente fiscal (HOMOLOGACAO ou PRODUCAO).
     * @return Lista de configurações correspondentes ao tipo informado.
     */
    List<ConfiguracaoFiscal> findByTipoAmbiente(TipoAmbiente tipoAmbiente);

    /**
     * Verifica se já existe uma configuração cadastrada para a empresa informada.
     *
     * @param empresaId ID da empresa.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByEmpresaId(Long empresaId);
}