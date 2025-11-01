package br.com.erp.ms_ativos.repository;

import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import br.com.erp.ms_ativos.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Ativo}.
 *
 * Fornece métodos personalizados para consultas específicas de ativos,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    /**
     * Busca um ativo pelo seu código patrimonial.
     *
     * @param codigoPatrimonial Código patrimonial único do ativo.
     * @return Optional contendo o ativo encontrado, se existir.
     */
    Optional<Ativo> findByCodigoPatrimonial(String codigoPatrimonial);

    /**
     * Lista todos os ativos de um tipo específico.
     *
     * @param tipo Tipo de ativo (EQUIPAMENTO, VEICULO, etc.).
     * @return Lista de ativos do tipo informado.
     */
    List<Ativo> findByTipo(TipoAtivo tipo);

    /**
     * Lista todos os ativos com um determinado status.
     *
     * @param status Status atual do ativo (ATIVO, EM_MANUTENCAO, etc.).
     * @return Lista de ativos com o status informado.
     */
    List<Ativo> findByStatus(StatusAtivo status);

    /**
     * Lista todos os ativos pertencentes a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de ativos vinculados à empresa.
     */
    List<Ativo> findByEmpresaId(Long empresaId);

    /**
     * Lista todos os ativos pertencentes a uma filial específica.
     *
     * @param filialId ID da filial.
     * @return Lista de ativos vinculados à filial.
     */
    List<Ativo> findByFilialId(Long filialId);
}