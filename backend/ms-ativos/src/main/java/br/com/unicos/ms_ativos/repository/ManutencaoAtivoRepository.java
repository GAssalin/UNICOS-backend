package br.com.unicos.ms_ativos.repository;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
import br.com.unicos.ms_ativos.model.ManutencaoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ManutencaoAtivo}.
 *
 * Fornece métodos personalizados para consultas específicas de manutenções de ativos,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface ManutencaoAtivoRepository extends JpaRepository<ManutencaoAtivo, Long> {

    /**
     * Lista todas as manutenções realizadas em um ativo específico.
     *
     * @param ativoId ID do ativo.
     * @return Lista de manutenções associadas ao ativo.
     */
    List<ManutencaoAtivo> findByAtivoId(Long ativoId);

    /**
     * Lista todas as manutenções de um tipo específico.
     *
     * @param tipo Tipo de manutenção (PREVENTIVA ou CORRETIVA).
     * @return Lista de manutenções do tipo informado.
     */
    List<ManutencaoAtivo> findByTipo(TipoManutencao tipo);

    /**
     * Lista todas as manutenções com um determinado status.
     *
     * @param status Status atual da manutenção (AGENDADA, CONCLUIDA, etc.).
     * @return Lista de manutenções com o status informado.
     */
    List<ManutencaoAtivo> findByStatus(StatusManutencao status);

    /**
     * Lista todas as manutenções realizadas dentro de um intervalo de datas.
     *
     * @param inicio Data inicial do intervalo.
     * @param fim Data final do intervalo.
     * @return Lista de manutenções realizadas no período informado.
     */
    List<ManutencaoAtivo> findByDataManutencaoBetween(LocalDate inicio, LocalDate fim);
}