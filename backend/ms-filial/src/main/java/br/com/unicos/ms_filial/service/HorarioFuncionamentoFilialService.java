package br.com.unicos.ms_filial.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_filial.client.PermissaoClient;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialResponse;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialUpdateRequest;
import br.com.unicos.ms_filial.mapper.HorarioFuncionamentoFilialMapper;
import br.com.unicos.ms_filial.model.HorarioFuncionamentoFilial;
import br.com.unicos.ms_filial.repository.HorarioFuncionamentoFilialRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;

@Service
@Transactional
public class HorarioFuncionamentoFilialService extends BaseTenantService<HorarioFuncionamentoFilial, Long> {

    private final HorarioFuncionamentoFilialRepository horarioRepository;
    private final HorarioFuncionamentoFilialMapper horarioMapper;
    private final PermissaoClient permissaoClient;

    public HorarioFuncionamentoFilialService(
            HorarioFuncionamentoFilialRepository horarioRepository,
            HorarioFuncionamentoFilialMapper horarioMapper,
            PermissaoClient permissaoClient
    ) {
        super(horarioRepository);
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;
        this.permissaoClient = permissaoClient;
    }

    @CircuitBreaker(name = "filial-horario-admin", fallbackMethod = "fallbackAdmin")
    public HorarioFuncionamentoFilialResponse salvar(HorarioFuncionamentoFilialCreateRequest request) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_HORARIO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar horários de filial.");

        validarDiaDuplicado(request.filialId(), request.diaSemana());

        HorarioFuncionamentoFilial entity = horarioMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return horarioMapper.toResponse(horarioRepository.save(entity));
    }

    @CircuitBreaker(name = "filial-horario-admin", fallbackMethod = "fallbackAdmin")
    public HorarioFuncionamentoFilialResponse atualizar(Long id, HorarioFuncionamentoFilialUpdateRequest request) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_HORARIO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar horários de filial.");

        HorarioFuncionamentoFilial entity = buscarHorario(id);

        boolean mudouDia = !entity.getFilialId().equals(request.filialId())
                || !entity.getDiaSemana().equals(request.diaSemana());

        if (mudouDia)
            validarDiaDuplicado(request.filialId(), request.diaSemana());

        horarioMapper.updateEntity(request, entity);

        return horarioMapper.toResponse(horarioRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-horario-admin", fallbackMethod = "fallbackAdminId")
    public HorarioFuncionamentoFilialResponse buscarPorId(Long id) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_HORARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar horários de filial.");
        return horarioMapper.toResponse(buscarHorario(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-horario-admin", fallbackMethod = "fallbackAdminPage")
    public Page<HorarioFuncionamentoFilialResponse> listarPorFilial(Long filialId, Pageable pageable) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_HORARIO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar horários de filial.");
        return horarioRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(horarioMapper::toResponse);
    }

    @CircuitBreaker(name = "filial-horario-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_HORARIO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir horários de filial.");
        horarioRepository.delete(buscarHorario(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private HorarioFuncionamentoFilialResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de horários de filial temporariamente indisponível");
    }

    private HorarioFuncionamentoFilialResponse fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de horários de filial temporariamente indisponível");
    }

    private Page<HorarioFuncionamentoFilialResponse> fallbackAdminPage(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de horários de filial temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de horários de filial temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private HorarioFuncionamentoFilial buscarHorario(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário de filial não encontrado: " + id));
    }

    private void validarDiaDuplicado(Long filialId, DayOfWeek diaSemana) {
        if (horarioRepository.existsByFilialIdAndDiaSemanaAndEmpresaId(
                filialId,
                diaSemana,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe horário cadastrado para este dia da semana nesta filial.");
        }
    }
}
