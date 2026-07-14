package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import br.com.unicos.ms_estoque.mapper.ResponsavelEstoqueMapper;
import br.com.unicos.ms_estoque.model.ResponsavelEstoque;
import br.com.unicos.ms_estoque.repository.ResponsavelEstoqueRepository;
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
 * Service responsável pelas regras de negócio e operações do agregado {@link ResponsavelEstoque}.
 */
@Service
@Transactional
public class ResponsavelEstoqueService extends BaseTenantService<ResponsavelEstoque, Long> {

    private final ResponsavelEstoqueRepository responsavelRepository;
    private final ResponsavelEstoqueMapper responsavelMapper;

    /**
     * Construtor da service de responsáveis de estoque.
     *
     * @param responsavelRepository repositório do vínculo de responsável por estoque
     * @param responsavelMapper mapper de conversão entre entidade e DTOs
     */
    public ResponsavelEstoqueService(ResponsavelEstoqueRepository responsavelRepository, ResponsavelEstoqueMapper responsavelMapper) {
        super(responsavelRepository);
        this.responsavelRepository = responsavelRepository;
        this.responsavelMapper = responsavelMapper;
    }

    /**
     * Cria um novo vínculo de responsável para um estoque da empresa corrente.
     *
     * @param request dados de criação do vínculo
     * @return vínculo criado
     */
    public ResponsavelEstoqueResponseDto salvar(ResponsavelEstoqueCreateRequestDto request) {
        validarRegrasNegocio(
                request.estoqueId(),
                request.principal(),
                request.statusResponsavelEstoque(),
                request.vigenciaInicio(),
                request.vigenciaFim(),
                null
        );

        ResponsavelEstoque entity = responsavelMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return responsavelMapper.toResponse(save(entity));
    }

    /**
     * Atualiza um vínculo existente de responsável por estoque.
     *
     * @param id identificador do vínculo
     * @param request dados de atualização
     * @return vínculo atualizado
     */
    public ResponsavelEstoqueResponseDto atualizar(Long id, ResponsavelEstoqueUpdateRequestDto request) {
        ResponsavelEstoque entity = buscarResponsavel(id);

        validarRegrasNegocio(
                request.estoqueId(),
                request.principal(),
                request.statusResponsavelEstoque(),
                request.vigenciaInicio(),
                request.vigenciaFim(),
                id
        );

        responsavelMapper.updateEntity(request, entity);

        return responsavelMapper.toResponse(save(entity));
    }

    /**
     * Busca um vínculo por identificador.
     *
     * @param id identificador do vínculo
     * @return vínculo encontrado
     */
    @Transactional(readOnly = true)
    public ResponsavelEstoqueResponseDto buscarPorId(Long id) {
        return responsavelMapper.toResponse(buscarResponsavel(id));
    }

    /**
     * Lista os responsáveis de um estoque.
     *
     * @param estoqueId identificador do estoque
     * @param pageable paginação
     * @return página de responsáveis vinculados ao estoque
     */
    @Transactional(readOnly = true)
    public Page<ResponsavelEstoqueResponseDto> listarPorEstoque(Long estoqueId, Pageable pageable) {
        return responsavelRepository
                .findByEstoqueIdAndEmpresaId(estoqueId, TenantContext.getEmpresaId(), pageable)
                .map(responsavelMapper::toResponse);
    }

    /**
     * Lista os responsáveis de um estoque filtrados por status.
     *
     * @param estoqueId identificador do estoque
     * @param status status do vínculo
     * @param pageable paginação
     * @return página de responsáveis filtrados
     */
    @Transactional(readOnly = true)
    public Page<ResponsavelEstoqueResponseDto> listarPorEstoqueEStatus(
            Long estoqueId,
            StatusResponsavelEstoque status,
            Pageable pageable
    ) {
        return responsavelRepository
                .findByEstoqueIdAndStatusResponsavelEstoqueAndEmpresaId(
                        estoqueId,
                        status,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(responsavelMapper::toResponse);
    }

    /**
     * Remove um vínculo de responsável por estoque.
     *
     * @param id identificador do vínculo
     */
    public void deletar(Long id) {
        responsavelRepository.delete(buscarResponsavel(id));
    }

    /**
     * Busca um vínculo de responsável e garante que ele pertence ao tenant corrente.
     *
     * @param id identificador do vínculo
     * @return entidade encontrada
     */
    private ResponsavelEstoque buscarResponsavel(Long id) {
        ResponsavelEstoque entity = responsavelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Responsável do estoque não encontrado: " + id));

        if (!TenantContext.getEmpresaId().equals(entity.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao responsável fora do tenant.");

        return entity;
    }

    /**
     * Executa as validações de negócio do vínculo.
     *
     * @param estoqueId identificador do estoque
     * @param principal indicador de principal
     * @param status status do vínculo
     * @param vigenciaInicio data inicial da vigência
     * @param vigenciaFim data final da vigência
     * @param ignorarId identificador a ser ignorado na validação de unicidade
     */
    private void validarRegrasNegocio(
            Long estoqueId,
            Boolean principal,
            StatusResponsavelEstoque status,
            LocalDate vigenciaInicio,
            LocalDate vigenciaFim,
            Long ignorarId
    ) {
        validarVigencia(vigenciaInicio, vigenciaFim);
        validarPrincipalUnicoAtivo(estoqueId, principal, status, ignorarId);
    }

    /**
     * Garante que a vigência final não seja anterior à vigência inicial.
     *
     * @param vigenciaInicio data inicial
     * @param vigenciaFim data final
     */
    private void validarVigencia(LocalDate vigenciaInicio, LocalDate vigenciaFim) {
        if (vigenciaFim != null && vigenciaFim.isBefore(vigenciaInicio))
            throw new IllegalArgumentException("A vigência final não pode ser anterior à vigência inicial.");
    }

    /**
     * Garante que exista no máximo um responsável principal ativo por estoque no tenant.
     *
     * @param estoqueId identificador do estoque
     * @param principal indicador de principal
     * @param status status do vínculo
     * @param ignorarId identificador a ser ignorado na atualização
     */
    private void validarPrincipalUnicoAtivo(Long estoqueId, Boolean principal, StatusResponsavelEstoque status, Long ignorarId) {
        if (!Boolean.TRUE.equals(principal) || status != StatusResponsavelEstoque.ATIVO)
            return;

        boolean existeOutroPrincipalAtivo = responsavelRepository
                .findByEstoqueIdAndPrincipalAndEmpresaId(estoqueId, true, TenantContext.getEmpresaId())
                .filter(responsavel -> responsavel.getStatusResponsavelEstoque() == StatusResponsavelEstoque.ATIVO)
                .filter(responsavel -> !responsavel.getId().equals(ignorarId))
                .isPresent();

        if (existeOutroPrincipalAtivo)
            throw new IllegalArgumentException("Já existe um responsável principal ativo para este estoque neste tenant.");
    }

}
