package br.com.unicos.ms_empresa.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.mapper.EmpresaEnderecoMapper;
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

    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackCriar")
    public EmpresaEnderecoResponse criar(EmpresaEnderecoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarEnderecoDuplicado(
                request.logradouro(),
                request.numero(),
                request.cep(),
                empresaId
        );

        if (request.principal()) {
            removerEnderecoPrincipalAtual(empresaId);
        }

        EmpresaEndereco endereco = mapper.toEntity(request);
        endereco.setEmpresaId(empresaId);

        return mapper.toResponseDTO(repository.save(endereco));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackAtualizar")
    public EmpresaEnderecoResponse atualizar(Long id, EmpresaEnderecoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        EmpresaEndereco endereco = buscarEndereco(id, empresaId);

        boolean alterouEndereco =
                !endereco.getLogradouro().equalsIgnoreCase(request.logradouro())
                        || !endereco.getNumero().equalsIgnoreCase(request.numero())
                        || !endereco.getCep().equalsIgnoreCase(request.cep());

        if (alterouEndereco) {
            validarEnderecoDuplicado(
                    request.logradouro(),
                    request.numero(),
                    request.cep(),
                    empresaId
            );
        }

        if (request.principal()) {
            removerEnderecoPrincipalAtual(empresaId, id);
        }

        mapper.updateEntityFromDTO(request, endereco);

        return mapper.toResponseDTO(repository.save(endereco));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackBuscarPorId")
    public EmpresaEnderecoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponseDTO(buscarEndereco(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackListar")
    public Page<EmpresaEnderecoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoDTO);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackListarPorTipo")
    public Page<EmpresaEnderecoResumoResponse> listarPorTipo(
            TipoEnderecoEmpresa tipo,
            Pageable pageable
    ) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByTipoEnderecoAndEmpresaId(tipo, empresaId, pageable)
                .map(mapper::toResumoDTO);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "empresa-endereco-admin", fallbackMethod = "fallbackRemover")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarEndereco(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private EmpresaEnderecoResponse fallbackCriar(EmpresaEnderecoCreateRequest request, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private EmpresaEnderecoResponse fallbackAtualizar(
            Long id,
            EmpresaEnderecoUpdateRequest request,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private EmpresaEnderecoResponse fallbackBuscarPorId(Long id, Throwable ex) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaEnderecoResumoResponse> fallbackListar(
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private Page<EmpresaEnderecoResumoResponse> fallbackListarPorTipo(
            TipoEnderecoEmpresa tipo,
            Pageable pageable,
            Throwable ex
    ) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de endereços da empresa temporariamente indisponível"
        );
    }

    private void fallbackRemover(Long id, Throwable ex) {
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
                .orElseThrow(() ->
                        new EntityNotFoundException("Endereço institucional não encontrado: " + id));
    }

    private void validarEnderecoDuplicado(
            String logradouro,
            String numero,
            String cep,
            Long empresaId
    ) {
        if (repository.existsByLogradouroAndNumeroAndCepAndEmpresaId(
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

    private void removerEnderecoPrincipalAtual(Long empresaId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .ifPresent(endereco -> {
                    endereco.setPrincipal(false);
                    repository.save(endereco);
                });
    }

    private void removerEnderecoPrincipalAtual(Long empresaId, Long enderecoAtualId) {
        repository.findByPrincipalTrueAndEmpresaId(empresaId)
                .filter(endereco -> !endereco.getId().equals(enderecoAtualId))
                .ifPresent(endereco -> {
                    endereco.setPrincipal(false);
                    repository.save(endereco);
                });
    }
}