package br.com.unicos.ms_filial.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_filial.client.AuthClient;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialResponse;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialUpdateRequest;
import br.com.unicos.ms_filial.mapper.EnderecoFilialMapper;
import br.com.unicos.ms_filial.model.EnderecoFilial;
import br.com.unicos.ms_filial.repository.EnderecoFilialRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class EnderecoFilialService extends BaseTenantService<EnderecoFilial, Long> {

    private final EnderecoFilialRepository enderecoRepository;
    private final EnderecoFilialMapper enderecoMapper;
    private final AuthClient authClient;

    public EnderecoFilialService(
            EnderecoFilialRepository enderecoRepository,
            EnderecoFilialMapper enderecoMapper,
            AuthClient authClient
    ) {
        super(enderecoRepository);
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.authClient = authClient;
    }

    @CircuitBreaker(name = "filial-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EnderecoFilialResponse salvar(EnderecoFilialCreateRequest request) {
        if (!authClient.usuarioPossuiPermissao("FILIAL_ENDERECO_CRIAR"))
            throw new AccessDeniedException("Usuário não possui permissão para criar endereços de filial.");

        validarDuplicidade(request);

        EnderecoFilial entity = enderecoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return enderecoMapper.toResponse(enderecoRepository.save(entity));
    }

    @CircuitBreaker(name = "filial-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EnderecoFilialResponse atualizar(Long id, EnderecoFilialUpdateRequest request) {
        if (!authClient.usuarioPossuiPermissao("FILIAL_ENDERECO_EDITAR"))
            throw new AccessDeniedException("Usuário não possui permissão para editar endereços de filial.");

        EnderecoFilial entity = buscarEndereco(id);

        boolean mudouChave = !entity.getFilialId().equals(request.filialId())
                || !entity.getLogradouro().equalsIgnoreCase(request.logradouro())
                || !entity.getNumero().equalsIgnoreCase(request.numero())
                || !entity.getCep().equals(request.cep());

        if (mudouChave)
            validarDuplicidade(request);

        enderecoMapper.updateEntity(request, entity);

        return enderecoMapper.toResponse(enderecoRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-endereco-admin", fallbackMethod = "fallbackAdminId")
    public EnderecoFilialResponse buscarPorId(Long id) {
        if (!authClient.usuarioPossuiPermissao("FILIAL_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para visualizar endereços de filial.");
        return enderecoMapper.toResponse(buscarEndereco(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "filial-endereco-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EnderecoFilialResponse> listarPorFilial(Long filialId, Pageable pageable) {
        if (!authClient.usuarioPossuiPermissao("FILIAL_ENDERECO_LISTAR"))
            throw new AccessDeniedException("Usuário não possui permissão para listar endereços de filial.");
        return enderecoRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
                .map(enderecoMapper::toResponse);
    }

    @CircuitBreaker(name = "filial-endereco-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!authClient.usuarioPossuiPermissao("FILIAL_ENDERECO_EXCLUIR"))
            throw new AccessDeniedException("Usuário não possui permissão para excluir endereços de filial.");
        enderecoRepository.delete(buscarEndereco(id));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EnderecoFilialResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de filial temporariamente indisponível");
    }

    private EnderecoFilialResponse fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de filial temporariamente indisponível");
    }

    private Page<EnderecoFilialResponse> fallbackAdminPage(Long filialId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de filial temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de endereços de filial temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EnderecoFilial buscarEndereco(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço de filial não encontrado: " + id));
    }

    private void validarDuplicidade(EnderecoFilialCreateRequest request) {
        if (enderecoRepository.existsByFilialIdAndLogradouroAndNumeroAndCepAndEmpresaId(
                request.filialId(),
                request.logradouro(),
                request.numero(),
                request.cep(),
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe um endereço com o mesmo logradouro/número/CEP para esta filial.");
        }
    }

    private void validarDuplicidade(EnderecoFilialUpdateRequest request) {
        if (enderecoRepository.existsByFilialIdAndLogradouroAndNumeroAndCepAndEmpresaId(
                request.filialId(),
                request.logradouro(),
                request.numero(),
                request.cep(),
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe um endereço com o mesmo logradouro/número/CEP para esta filial.");
        }
    }
}
