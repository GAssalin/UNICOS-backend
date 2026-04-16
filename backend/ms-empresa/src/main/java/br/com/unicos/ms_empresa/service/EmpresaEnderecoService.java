package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaEnderecoMapper;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import br.com.unicos.ms_empresa.repository.EmpresaEnderecoRepository;
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
public class EmpresaEnderecoService extends BaseTenantService<EmpresaEndereco, Long> {

    private final EmpresaEnderecoRepository repository;
    private final EmpresaEnderecoMapper mapper;

    public EmpresaEnderecoService(
            EmpresaEnderecoRepository repository,
            EmpresaEnderecoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaEnderecoResponse criar(EmpresaEnderecoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(request.empresaRefId());

        validarEnderecoDuplicado(
                empresa,
                request.logradouro(),
                request.numero(),
                request.cep(),
                empresaId
        );

        if (request.principal()) {
            removerEnderecoPrincipalAtual(empresa, empresaId);
        }

        EmpresaEndereco endereco = mapper.toEntity(request);
        endereco.setEmpresaId(empresaId);

        return mapper.toResponseDTO(repository.save(endereco));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaEnderecoResponse atualizar(Long id, EmpresaEnderecoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaEndereco endereco = buscarEndereco(id, empresaId);

        boolean alterouEndereco =
                !endereco.getLogradouro().equalsIgnoreCase(request.logradouro())
                        || !endereco.getNumero().equalsIgnoreCase(request.numero())
                        || !endereco.getCep().equalsIgnoreCase(request.cep());

        if (alterouEndereco) {
            validarEnderecoDuplicado(
                    endereco.getEmpresa(),
                    request.logradouro(),
                    request.numero(),
                    request.cep(),
                    empresaId
            );
        }

        if (request.principal()) {
            removerEnderecoPrincipalAtual(endereco.getEmpresa(), empresaId);
            endereco.setPrincipal(true);
        } else {
            endereco.setPrincipal(false);
        }

        mapper.updateEntityFromDTO(request, endereco);

        return mapper.toResponseDTO(repository.save(endereco));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAdmin")
    public EmpresaEnderecoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponseDTO(buscarEndereco(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAdminPage")
    public Page<EmpresaEnderecoResumoResponse> listar(Long empresaRefId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndEmpresaId(empresa, empresaId, pageable)
                .map(mapper::toResumoDTO);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAdminPageTipo")
    public Page<EmpresaEnderecoResumoResponse> listarPorTipo(
            Long empresaRefId,
            TipoEnderecoEmpresa tipo,
            Pageable pageable
    ) {
        Long empresaId = TenantContext.getEmpresaId();
        Empresa empresa = empresaRef(empresaRefId);

        return repository.findByEmpresaAndTipoEnderecoAndEmpresaId(
                        empresa,
                        tipo,
                        empresaId,
                        pageable
                )
                .map(mapper::toResumoDTO);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarEndereco(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaEnderecoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaEnderecoResumoResponse> fallbackAdminPage(
            Long empresaRefId,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaEnderecoResumoResponse> fallbackAdminPageTipo(
            Long empresaRefId,
            TipoEnderecoEmpresa tipo,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private EmpresaEndereco buscarEndereco(Long id, Long empresaId) {
        return repository.findById(id)
                .filter(endereco -> empresaId.equals(endereco.getEmpresaId()))
                .orElseThrow(() -> new EntityNotFoundException("Endereço institucional não encontrado: " + id));
    }

    private void validarEnderecoDuplicado(
            Empresa empresa,
            String logradouro,
            String numero,
            String cep,
            Long empresaId
    ) {
        if (repository.existsByEmpresaAndLogradouroAndNumeroAndCepAndEmpresaId(
                empresa,
                logradouro,
                numero,
                cep,
                empresaId
        )) {
            throw new IllegalArgumentException(
                    "Já existe um endereço cadastrado com os dados informados para esta empresa."
            );
        }
    }

    private void removerEnderecoPrincipalAtual(Empresa empresa, Long empresaId) {
        repository.findByEmpresaAndPrincipalTrueAndEmpresaId(empresa, empresaId)
                .ifPresent(endereco -> {
                    endereco.setPrincipal(false);
                    repository.save(endereco);
                });
    }

    private Empresa empresaRef(Long empresaRefId) {
        return Empresa.builder()
                .id(empresaRefId)
                .build();
    }

}