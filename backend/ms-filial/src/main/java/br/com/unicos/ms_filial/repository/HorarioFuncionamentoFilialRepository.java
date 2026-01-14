package br.com.unicos.ms_filial.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_filial.model.HorarioFuncionamentoFilial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link HorarioFuncionamentoFilial}.
 * <p>
 * Centraliza consultas relacionadas aos horários de funcionamento das filiais,
 * respeitando o contexto multi-tenant.
 * </p>
 */
@Repository
public interface HorarioFuncionamentoFilialRepository extends BaseTenantRepository<HorarioFuncionamentoFilial, Long> {

    /**
     * Lista horários por filial dentro do tenant com paginação.
     */
    Page<HorarioFuncionamentoFilial> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o horário de um dia específico por filial dentro do tenant.
     */
    Optional<HorarioFuncionamentoFilial> findByFilialIdAndDiaSemanaAndEmpresaId(
            Long filialId,
            DayOfWeek diaSemana,
            Long tenantId
    );

    /**
     * Verifica se já existe cadastro de horário para o dia na filial dentro do tenant.
     */
    boolean existsByFilialIdAndDiaSemanaAndEmpresaId(
            Long filialId,
            DayOfWeek diaSemana,
            Long tenantId
    );

    /**
     * Remove todos os horários de uma filial dentro do tenant.
     */
    void deleteByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId
    );
}
