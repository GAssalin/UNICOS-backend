package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusEstoque;
import br.com.unicos.ms_estoque.mapper.EstoqueMapper;
import br.com.unicos.ms_estoque.model.Estoque;
import br.com.unicos.ms_estoque.repository.EstoqueRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsável pelas regras de negócio e operações do agregado {@link Estoque}.
 */
@Service
@Transactional
public class EstoqueService extends BaseTenantService<Estoque, Long> {

    private static final String DUPLICATE_CODE_MESSAGE = "Já existe um estoque com o código informado neste tenant.";

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;

    /**
     * Construtor da service de estoque.
     *
     * @param estoqueRepository repositório de estoque
     * @param estoqueMapper mapper de conversão entre entidade e DTOs
     */
    public EstoqueService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper) {
        super(estoqueRepository);
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
    }

    /**
     * Cria um novo estoque para a empresa corrente.
     *
     * @param request dados de criação do estoque
     * @return estoque criado
     */
    public EstoqueResponseDto salvar(EstoqueCreateRequestDto request) {
        validarCodigoDuplicado(request.codigo());
        validarEstoquePai(request.estoquePaiId(), null);

        Estoque entity = estoqueMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return estoqueMapper.toResponse(save(entity));
    }

    /**
     * Atualiza um estoque existente.
     *
     * @param id identificador do estoque
     * @param request dados de atualização
     * @return estoque atualizado
     */
    public EstoqueResponseDto atualizar(Long id, EstoqueUpdateRequestDto request) {
        Estoque entity = buscarEstoque(id);

        if (!entity.getCodigo().equalsIgnoreCase(request.codigo()))
            validarCodigoDuplicado(request.codigo());

        validarEstoquePai(request.estoquePaiId(), id);

        estoqueMapper.updateEntity(request, entity);

        return estoqueMapper.toResponse(save(entity));
    }

    /**
     * Busca um estoque por identificador.
     *
     * @param id identificador do estoque
     * @return estoque encontrado
     */
    @Transactional(readOnly = true)
    public EstoqueResponseDto buscarPorId(Long id) {
        return estoqueMapper.toResponse(buscarEstoque(id));
    }

    /**
     * Lista todos os estoques da empresa corrente de forma paginada.
     *
     * @param pageable paginação
     * @return página de estoques
     */
    @Transactional(readOnly = true)
    public Page<EstoqueResponseDto> listar(Pageable pageable) {
        return findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(estoqueMapper::toResponse);
    }

    /**
     * Lista os estoques filtrados por status.
     *
     * @param status status do estoque
     * @param pageable paginação
     * @return página de estoques
     */
    @Transactional(readOnly = true)
    public Page<EstoqueResponseDto> listarPorStatus(StatusEstoque status, Pageable pageable) {
        return estoqueRepository
                .findByStatusEstoqueAndEmpresaId(status, TenantContext.getEmpresaId(), pageable)
                .map(estoqueMapper::toResponse);
    }

    /**
     * Lista os estoques filhos de um estoque pai.
     *
     * @param estoquePaiId identificador do estoque pai
     * @param pageable paginação
     * @return página de estoques filhos
     */
    @Transactional(readOnly = true)
    public Page<EstoqueResponseDto> listarFilhos(Long estoquePaiId, Pageable pageable) {
        buscarEstoque(estoquePaiId);

        return estoqueRepository
                .findByEstoquePaiIdAndEmpresaId(estoquePaiId, TenantContext.getEmpresaId(), pageable)
                .map(estoqueMapper::toResponse);
    }

    /**
     * Remove um estoque da empresa corrente.
     *
     * @param id identificador do estoque
     */
    public void deletar(Long id) {
        Estoque entity = buscarEstoque(id);
        validarSePossuiFilhos(id);
        estoqueRepository.delete(entity);
    }

    /**
     * Busca um estoque e garante que ele pertence ao tenant corrente.
     *
     * @param id identificador do estoque
     * @return entidade encontrada
     */
    private Estoque buscarEstoque(Long id) {
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado: " + id));

        if (!TenantContext.getEmpresaId().equals(entity.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao estoque fora do tenant.");

        return entity;
    }

    /**
     * Valida se já existe outro estoque com o mesmo código para a empresa corrente.
     *
     * @param codigo código a ser validado
     */
    private void validarCodigoDuplicado(String codigo) {
        if (estoqueRepository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException(DUPLICATE_CODE_MESSAGE);
    }

    /**
     * Valida a consistência do estoque pai informado.
     *
     * @param estoquePaiId identificador do estoque pai
     * @param idEstoqueAtual identificador do estoque atual em caso de update
     */
    private void validarEstoquePai(Long estoquePaiId, Long idEstoqueAtual) {
        if (estoquePaiId == null)
            return;
        if (idEstoqueAtual != null && idEstoqueAtual.equals(estoquePaiId))
            throw new IllegalArgumentException("Um estoque não pode ser pai de si mesmo.");

        buscarEstoque(estoquePaiId);
    }

    /**
     * Impede a exclusão de um estoque que ainda possui filhos vinculados.
     *
     * @param estoqueId identificador do estoque
     */
    private void validarSePossuiFilhos(Long estoqueId) {
        boolean possuiFilhos = estoqueRepository
                .findByEstoquePaiIdAndEmpresaId(estoqueId, TenantContext.getEmpresaId(), PageRequest.of(0, 1))
                .hasContent();

        if (possuiFilhos)
            throw new IllegalStateException("Não é possível excluir um estoque que possui estoques filhos vinculados.");
    }

}
