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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service responsável pelas regras de negócio e operações de {@link VinculoEstoqueFilial}.
 */
@Service
@Transactional
public class VinculoEstoqueFilialService extends BaseTenantService<VinculoEstoqueFilial, Long> {

    private final VinculoEstoqueFilialRepository vinculoRepository;
    private final VinculoEstoqueFilialMapper vinculoMapper;

    /**
     * Construtor da service de vínculo entre estoque e filial.
     *
     * @param vinculoRepository repositório de vínculos
     * @param vinculoMapper mapper de conversão entre entidade e DTOs
     */
    public VinculoEstoqueFilialService(VinculoEstoqueFilialRepository vinculoRepository, VinculoEstoqueFilialMapper vinculoMapper) {
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
    public VinculoEstoqueFilialResponseDto salvar(VinculoEstoqueFilialCreateRequestDto request) {
        validarDuplicidadeParEstoqueFilial(request.estoqueId(), request.filialId(), null);
        validarVigencia(request.vigenciaInicio(), request.vigenciaFim());

        VinculoEstoqueFilial entity = vinculoMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return vinculoMapper.toResponse(save(entity));
    }

    /**
     * Atualiza um vínculo existente.
     *
     * @param id identificador do vínculo
     * @param request dados de atualização
     * @return vínculo atualizado
     */
    public VinculoEstoqueFilialResponseDto atualizar(Long id, VinculoEstoqueFilialUpdateRequestDto request) {
        VinculoEstoqueFilial entity = buscarVinculo(id);

        if (!entity.getEstoqueId().equals(request.estoqueId()) || !entity.getFilialId().equals(request.filialId()))
            validarDuplicidadeParEstoqueFilial(request.estoqueId(), request.filialId(), id);

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
    public Page<VinculoEstoqueFilialResponseDto> listarPorFilial(Long filialId, Pageable pageable) {
        return vinculoRepository
                .findByFilialIdAndEmpresaId(filialId, TenantContext.getEmpresaId(), pageable)
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
    public Page<VinculoEstoqueFilialResponseDto> listarPorFilialEStatus(Long filialId, StatusVinculoEstoqueFilial status, Pageable pageable) {
        return vinculoRepository
                .findByFilialIdAndStatusVinculoEstoqueFilialAndEmpresaId(
                        filialId,
                        status,
                        TenantContext.getEmpresaId(),
                        pageable
                )
                .map(vinculoMapper::toResponse);
    }

    /**
     * Remove um vínculo da empresa corrente.
     *
     * @param id identificador do vínculo
     */
    public void deletar(Long id) {
        vinculoRepository.delete(buscarVinculo(id));
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

        if (!TenantContext.getEmpresaId().equals(entity.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao vínculo fora do tenant.");

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
                .findByEstoqueIdAndFilialIdAndEmpresaId(estoqueId, filialId, TenantContext.getEmpresaId())
                .filter(v -> !v.getId().equals(ignorarId))
                .isPresent();

        if (existe)
            throw new IllegalArgumentException("Já existe vínculo para este estoque e filial neste tenant.");
    }

    /**
     * Valida a consistência do período de vigência informado.
     *
     * @param vigenciaInicio data inicial
     * @param vigenciaFim data final
     */
    private void validarVigencia(LocalDate vigenciaInicio, LocalDate vigenciaFim) {
        if (vigenciaFim != null && vigenciaFim.isBefore(vigenciaInicio))
            throw new IllegalArgumentException("A vigência final não pode ser anterior à vigência inicial.");
    }

}
