package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialCreateRequestDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialResponseDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.mapper.VinculoEstoqueFilialMapper;
import br.com.unicos.ms_estoque.model.VinculoEstoqueFilial;
import br.com.unicos.ms_estoque.repository.VinculoEstoqueFilialRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

/**
 * Service responsável pelas regras de negócio e operações de {@link VinculoEstoqueFilial}.
 */
@Service
@Transactional
public class VinculoEstoqueFilialService extends BaseTenantService<VinculoEstoqueFilial, Long> {

    private static final String CIRCUIT_BREAKER_NAME = "estoque-vinculo-admin";
    private static final String FALLBACK_MESSAGE = "Serviço de estoques temporariamente indisponível.";
    private static final String DUPLICATE_LINK_MESSAGE = "Já existe vínculo para este estoque e filial neste tenant.";

    private final VinculoEstoqueFilialRepository vinculoRepository;
    private final VinculoEstoqueFilialMapper vinculoMapper;

    /**
     * Construtor da service de vínculo entre estoque e filial.
     *
     * @param vinculoRepository repositório de vínculos
     * @param vinculoMapper mapper de conversão entre entidade e DTOs
     */
    public VinculoEstoqueFilialService(
            VinculoEstoqueFilialRepository vinculoRepository,
            VinculoEstoqueFilialMapper vinculoMapper
    ) {
        super(vinculoRepository);
        this.vinculoRepository = vinculoRepository;
        this.vinculoMapper = vinculoMapper;
    }

    /**
     * Cria um novo vínculo entre estoque e filial.
     *
     * @param request dados de criação do vínculo
     * @return vínculo criado
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdmin")
    public VinculoEstoqueFilialResponseDto salvar(VinculoEstoqueFilialCreateRequestDto request) {
        validarDuplicidadeParEstoqueFilial(request.estoqueId(), request.filialId(), null);
        validarVigencia(request.vigenciaInicio(), request.vigenciaFim());

        VinculoEstoqueFilial entity = vinculoMapper.toEntity(request);
        entity.setEmpresaId(obterEmpresaId());

        return vinculoMapper.toResponse(save(entity));
    }

    /**
     * Atualiza um vínculo existente.
     *
     * @param id identificador do vínculo
     * @param request dados de atualização
     * @return vínculo atualizado
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminIdReq")
    public VinculoEstoqueFilialResponseDto atualizar(Long id, VinculoEstoqueFilialUpdateRequestDto request) {
        VinculoEstoqueFilial entity = buscarVinculo(id);

        if (!entity.getEstoqueId().equals(request.estoqueId()) || !entity.getFilialId().equals(request.filialId())) {
            validarDuplicidadeParEstoqueFilial(request.estoqueId(), request.filialId(), id);
        }

        validarVigencia(request.vigenciaInicio(), request.vigenciaFim());
        vinculoMapper.updateEntity(request, entity);

        return vinculoMapper.toResponse(save(entity));
    }

    /**
     * Busca um vínculo por identificador.
     *
     * @param id identificador do vínculo
     * @return vínculo encontrado
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminId")
    public VinculoEstoqueFilialResponseDto buscarPorId(Long id) {
        return vinculoMapper.toResponse(buscarVinculo(id));
    }

    /**
     * Lista os vínculos por filial.
     *
     * @param filialId identificador da filial
     * @param pageable paginação
     * @return página de vínculos
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminPageFilial")
    public Page<VinculoEstoqueFilialResponseDto> listarPorFilial(Long filialId, Pageable pageable) {
        return vinculoRepository
                .findByFilialIdAndEmpresaId(filialId, obterEmpresaId(), pageable)
                .map(vinculoMapper::toResponse);
    }

    /**
     * Lista os vínculos por filial e status.
     *
     * @param filialId identificador da filial
     * @param status status do vínculo
     * @param pageable paginação
     * @return página de vínculos
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminPageFilialStatus")
    public Page<VinculoEstoqueFilialResponseDto> listarPorFilialEStatus(
            Long filialId,
            StatusVinculoEstoqueFilial status,
            Pageable pageable
    ) {
        return vinculoRepository
                .findByFilialIdAndStatusVinculoEstoqueFilialAndEmpresaId(
                        filialId,
                        status,
                        obterEmpresaId(),
                        pageable
                )
                .map(vinculoMapper::toResponse);
    }

    /**
     * Remove um vínculo da empresa corrente.
     *
     * @param id identificador do vínculo
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        vinculoRepository.delete(buscarVinculo(id));
    }

    /**
     * Fallback para operações com request simples.
     *
     * @param req request recebido
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private VinculoEstoqueFilialResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para operações com identificador.
     *
     * @param id identificador recebido
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private VinculoEstoqueFilialResponseDto fallbackAdminId(Long id, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para operações com identificador e request.
     *
     * @param id identificador recebido
     * @param req request recebido
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private VinculoEstoqueFilialResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para listagem paginada por filial.
     *
     * @param filialId identificador da filial
     * @param pageable paginação recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private Page<VinculoEstoqueFilialResponseDto> fallbackAdminPageFilial(Long filialId, Pageable pageable, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para listagem paginada por filial e status.
     *
     * @param filialId identificador da filial
     * @param status status recebido
     * @param pageable paginação recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private Page<VinculoEstoqueFilialResponseDto> fallbackAdminPageFilialStatus(
            Long filialId,
            StatusVinculoEstoqueFilial status,
            Pageable pageable,
            Throwable ex
    ) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para deleção.
     *
     * @param id identificador recebido
     * @param ex exceção original
     */
    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Busca um vínculo e garante que ele pertence ao tenant corrente.
     *
     * @param id identificador do vínculo
     * @return entidade encontrada
     */
    private VinculoEstoqueFilial buscarVinculo(Long id) {
        VinculoEstoqueFilial entity = vinculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo estoque x filial não encontrado: " + id));

        if (!obterEmpresaId().equals(entity.getEmpresaId())) {
            throw new AccessDeniedException("Acesso negado ao vínculo fora do tenant.");
        }

        return entity;
    }

    /**
     * Valida se já existe vínculo com o mesmo par estoque e filial para a empresa corrente.
     *
     * @param estoqueId identificador do estoque
     * @param filialId identificador da filial
     * @param ignorarId identificador a ser ignorado na atualização
     */
    private void validarDuplicidadeParEstoqueFilial(Long estoqueId, Long filialId, Long ignorarId) {
        boolean existe = vinculoRepository
                .findByEstoqueIdAndFilialIdAndEmpresaId(estoqueId, filialId, obterEmpresaId())
                .filter(v -> !v.getId().equals(ignorarId))
                .isPresent();

        if (existe) {
            throw new IllegalArgumentException(DUPLICATE_LINK_MESSAGE);
        }
    }

    /**
     * Valida a consistência do período de vigência informado.
     *
     * @param vigenciaInicio data inicial
     * @param vigenciaFim data final
     */
    private void validarVigencia(LocalDate vigenciaInicio, LocalDate vigenciaFim) {
        if (vigenciaFim != null && vigenciaFim.isBefore(vigenciaInicio)) {
            throw new IllegalArgumentException("A vigência final não pode ser anterior à vigência inicial.");
        }
    }

    /**
     * Obtém o identificador da empresa do tenant corrente.
     *
     * @return identificador da empresa
     */
    private Long obterEmpresaId() {
        return TenantContext.getEmpresaId();
    }

    /**
     * Cria a exceção padrão de indisponibilidade do serviço.
     *
     * @param ex exceção original
     * @return exceção HTTP padronizada
     */
    private ResponseStatusException indisponibilidade(Throwable ex) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, FALLBACK_MESSAGE, ex);
    }
}
