package br.com.unicos.ms_filial.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialResponse;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialUpdateRequest;
import br.com.unicos.ms_filial.mapper.ContatoFilialMapper;
import br.com.unicos.ms_filial.model.ContatoFilial;
import br.com.unicos.ms_filial.repository.ContatoFilialRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ContatoFilialService extends BaseTenantService<ContatoFilial, Long> {

    private final ContatoFilialRepository contatoRepository;
    private final ContatoFilialMapper contatoMapper;

    public ContatoFilialService(
            ContatoFilialRepository contatoRepository,
            ContatoFilialMapper contatoMapper
    ) {
        super(contatoRepository);
        this.contatoRepository = contatoRepository;
        this.contatoMapper = contatoMapper;
    }

    @CircuitBreaker(name = "filial-contato-admin", fallbackMethod = "fallbackAdmin")
    public ContatoFilialResponse salvar(ContatoFilialCreateRequest request) {
        validarEmailDuplicado(request.filialId(), request.emailPrincipal());

        ContatoFilial entity = contatoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return contatoMapper.toResponse(contatoRepository.save(entity));
    }

    @CircuitBreaker(name = "filial-contato-admin", fallbackMethod = "fallbackAdmin")
    public ContatoFilialResponse atualizar(Long id, ContatoFilialUpdateRequest request) {
        ContatoFilial entity = buscarContato(id);

        if (!entity.getEmailPrincipal().equalsIgnoreCase(request.emailPrincipal()))
            validarEmailDuplicado(request.filialId(), request.emailPrincipal());

        contatoMapper.updateEntity(request, entity);

        return contatoMapper.toResponse(contatoRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-contato-admin", fallbackMethod = "fallbackAdminId")
    public ContatoFilialResponse buscarPorId(Long id) {
        return contatoMapper.toResponse(buscarContato(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-contato-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ContatoFilialResponse> listarPorFilial(Long filialId, Pageable pageable) {
        return contatoRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(contatoMapper::toResponse);
    }

    @CircuitBreaker(name = "filial-contato-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        contatoRepository.delete(buscarContato(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ContatoFilialResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de filial temporariamente indisponível");
    }

    private ContatoFilialResponse fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de filial temporariamente indisponível");
    }

    private Page<ContatoFilialResponse> fallbackAdminPage(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de filial temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de contatos de filial temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ContatoFilial buscarContato(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato de filial não encontrado: " + id));
    }

    private void validarEmailDuplicado(Long filialId, String emailPrincipal) {
        if (contatoRepository.existsByFilialIdAndEmailPrincipalAndEmpresaId(
                filialId,
                emailPrincipal,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe um contato com o e-mail principal informado para esta filial.");
        }
    }
}
