package br.com.unicos.ms_filial.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_filial.client.PermissaoClient;
import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoCreateRequest;
import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoResponse;
import br.com.unicos.ms_filial.mapper.FilialStatusHistoricoMapper;
import br.com.unicos.ms_filial.model.FilialStatusHistorico;
import br.com.unicos.ms_filial.repository.FilialStatusHistoricoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
public class FilialStatusHistoricoService extends BaseTenantService<FilialStatusHistorico, Long> {

    private final FilialStatusHistoricoRepository historicoRepository;
    private final FilialStatusHistoricoMapper historicoMapper;
    private final PermissaoClient permissaoClient;

    public FilialStatusHistoricoService(
            FilialStatusHistoricoRepository historicoRepository,
            FilialStatusHistoricoMapper historicoMapper,
            PermissaoClient permissaoClient
    ) {
        super(historicoRepository);
        this.historicoRepository = historicoRepository;
        this.historicoMapper = historicoMapper;
        this.permissaoClient = permissaoClient;
    }

    @CircuitBreaker(name = "filial-status-historico-admin", fallbackMethod = "fallbackAdmin")
    public FilialStatusHistoricoResponse salvar(FilialStatusHistoricoCreateRequest request) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_STATUS_HISTORICO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para registrar histórico de status.");

        FilialStatusHistorico entity = historicoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        // No MVP, normalmente a data é definida no service para garantir consistência
        if (entity.getDataAlteracao() == null)
            entity.setDataAlteracao(LocalDateTime.now());

        return historicoMapper.toResponse(historicoRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-status-historico-admin", fallbackMethod = "fallbackAdminId")
    public FilialStatusHistoricoResponse buscarPorId(Long id) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_STATUS_HISTORICO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar histórico de status.");
        return historicoMapper.toResponse(buscarHistorico(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-status-historico-admin", fallbackMethod = "fallbackAdminPage")
    public Page<FilialStatusHistoricoResponse> listarPorFilial(Long filialId, Pageable pageable) {
        if (!permissaoClient.usuarioPossuiPermissao("FILIAL_STATUS_HISTORICO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar histórico de status.");
        return historicoRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(historicoMapper::toResponse);
    }

    // MVP: histórico normalmente não deve ser editado/deletado.
    // Se quiser, eu crio atualizar/deletar, mas o recomendável é restringir.

    // ============================================================
    // FALLBACKS
    // ============================================================

    private FilialStatusHistoricoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de histórico de status temporariamente indisponível");
    }

    private FilialStatusHistoricoResponse fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de histórico de status temporariamente indisponível");
    }

    private Page<FilialStatusHistoricoResponse> fallbackAdminPage(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de histórico de status temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private FilialStatusHistorico buscarHistorico(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Histórico de status não encontrado: " + id));
    }
}
