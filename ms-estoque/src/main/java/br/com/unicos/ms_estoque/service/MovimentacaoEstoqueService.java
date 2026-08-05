package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueSearchRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;
import br.com.unicos.ms_estoque.mapper.MovimentacaoEstoqueMapper;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoque;
import br.com.unicos.ms_estoque.repository.MovimentacaoEstoqueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service responsável pelas regras de negócio e operações do agregado {@link MovimentacaoEstoque}.
 */
@Service
@Transactional
public class MovimentacaoEstoqueService extends BaseTenantService<MovimentacaoEstoque, Long> {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final MovimentacaoEstoqueMapper movimentacaoEstoqueMapper;

    /**
     * Construtor da service de movimentação de estoque.
     *
     * @param movimentacaoEstoqueRepository repositório da movimentação
     * @param movimentacaoEstoqueMapper mapper de conversão entre entidade e DTOs
     */
    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, MovimentacaoEstoqueMapper movimentacaoEstoqueMapper) {
        super(movimentacaoEstoqueRepository);
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.movimentacaoEstoqueMapper = movimentacaoEstoqueMapper;
    }

    /**
     * Cria uma nova movimentação de estoque para a empresa corrente.
     *
     * @param request dados de criação da movimentação
     * @return movimentação criada
     */
    public MovimentacaoEstoqueResponseDto salvar(MovimentacaoEstoqueCreateRequestDto request) {
        validarDocumentoReferenciaDuplicado(request.documentoReferencia());
        validarConsistenciaMovimentacao(
                request.tipoMovimentacao(),
                request.estoqueOrigemId(),
                request.estoqueDestinoId()
        );

        MovimentacaoEstoque entity = movimentacaoEstoqueMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return movimentacaoEstoqueMapper.toResponse(save(entity));
    }

    /**
     * Atualiza uma movimentação de estoque existente.
     *
     * @param id identificador da movimentação
     * @param request dados de atualização
     * @return movimentação atualizada
     */
    public MovimentacaoEstoqueResponseDto atualizar(Long id, MovimentacaoEstoqueUpdateRequestDto request) {
        MovimentacaoEstoque entity = buscarMovimentacao(id);

        if (documentoReferenciaAlterado(entity, request.documentoReferencia()))
            validarDocumentoReferenciaDuplicado(request.documentoReferencia());

        validarConsistenciaMovimentacao(
                request.tipoMovimentacao(),
                request.estoqueOrigemId(),
                request.estoqueDestinoId()
        );

        movimentacaoEstoqueMapper.updateEntity(request, entity);

        return movimentacaoEstoqueMapper.toResponse(save(entity));
    }

    /**
     * Busca uma movimentação por identificador.
     *
     * @param id identificador da movimentação
     * @return movimentação encontrada
     */
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueResponseDto buscarPorId(Long id) {
        return movimentacaoEstoqueMapper.toResponse(buscarMovimentacao(id));
    }

    /**
     * Lista todas as movimentações da empresa corrente de forma paginada.
     *
     * @param pageable paginação
     * @return página de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> listar(Pageable pageable) {
        return findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueMapper::toResponse);
    }

    /**
     * Lista movimentações por tipo.
     *
     * @param tipoMovimentacao tipo da movimentação
     * @param pageable paginação
     * @return página de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> listarPorTipo(TipoMovimentacaoEstoque tipoMovimentacao, Pageable pageable) {
        return movimentacaoEstoqueRepository
                .findByTipoMovimentacaoAndEmpresaId(tipoMovimentacao, TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueMapper::toResponse);
    }

    /**
     * Lista movimentações por status.
     *
     * @param statusMovimentacao status da movimentação
     * @param pageable paginação
     * @return página de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> listarPorStatus(StatusMovimentacaoEstoque statusMovimentacao, Pageable pageable) {
        return movimentacaoEstoqueRepository
                .findByStatusMovimentacaoAndEmpresaId(statusMovimentacao, TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueMapper::toResponse);
    }

    /**
     * Lista movimentações por estoque de origem.
     *
     * @param estoqueOrigemId identificador do estoque de origem
     * @param pageable paginação
     * @return página de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> listarPorEstoqueOrigem(Long estoqueOrigemId, Pageable pageable) {
        return movimentacaoEstoqueRepository
                .findByEstoqueOrigemIdAndEmpresaId(estoqueOrigemId, TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueMapper::toResponse);
    }

    /**
     * Lista movimentações por estoque de destino.
     *
     * @param estoqueDestinoId identificador do estoque de destino
     * @param pageable paginação
     * @return página de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> listarPorEstoqueDestino(Long estoqueDestinoId, Pageable pageable) {
        return movimentacaoEstoqueRepository
                .findByEstoqueDestinoIdAndEmpresaId(estoqueDestinoId, TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueMapper::toResponse);
    }

    /**
     * Busca uma movimentação pelo documento de referência.
     *
     * @param documentoReferencia documento de referência
     * @return movimentação encontrada
     */
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueResponseDto buscarPorDocumentoReferencia(String documentoReferencia) {
        MovimentacaoEstoque entity = movimentacaoEstoqueRepository
                .findByDocumentoReferenciaAndEmpresaId(documentoReferencia, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Movimentação não encontrada para o documento de referência: " + documentoReferencia
                ));

        return movimentacaoEstoqueMapper.toResponse(entity);
    }

    /**
     * Lista movimentações dentro de um intervalo de datas.
     *
     * @param dataInicial data inicial
     * @param dataFinal data final
     * @param pageable paginação
     * @return página de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> listarPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, Pageable pageable) {
        validarPeriodo(dataInicial, dataFinal);

        return movimentacaoEstoqueRepository
                .findByDataMovimentacaoBetweenAndEmpresaId(dataInicial, dataFinal, TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueMapper::toResponse);
    }

    /**
     * Pesquisa movimentações com base no DTO de filtros.
     *
     * <p>
     * Como o repositório atual não possui suporte a Specification/Criteria,
     * esta implementação aplica os filtros em memória sobre os registros do tenant.
     * Para grande volume de dados, o ideal é evoluir o repository para consultas dinâmicas.
     * </p>
     *
     * @param request filtros da pesquisa
     * @param pageable paginação
     * @return página filtrada de movimentações
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDto> pesquisar(MovimentacaoEstoqueSearchRequestDto request, Pageable pageable) {
        if (request == null)
            return listar(pageable);
        if (request.dataInicial() != null && request.dataFinal() != null)
            validarPeriodo(request.dataInicial(), request.dataFinal());

        List<MovimentacaoEstoque> filtrados = findAllByEmpresaId(TenantContext.getEmpresaId(), Pageable.unpaged())
                .stream()
                .filter(entity -> request.tipoMovimentacao() == null
                        || request.tipoMovimentacao().equals(entity.getTipoMovimentacao()))
                .filter(entity -> request.estoqueOrigemId() == null
                        || request.estoqueOrigemId().equals(entity.getEstoqueOrigemId()))
                .filter(entity -> request.estoqueDestinoId() == null
                        || request.estoqueDestinoId().equals(entity.getEstoqueDestinoId()))
                .filter(entity -> request.documentoReferencia() == null
                        || request.documentoReferencia().isBlank()
                        || (entity.getDocumentoReferencia() != null
                        && entity.getDocumentoReferencia().toLowerCase()
                        .contains(request.documentoReferencia().toLowerCase())))
                .filter(entity -> request.usuarioResponsavelId() == null
                        || request.usuarioResponsavelId().equals(entity.getUsuarioResponsavelId()))
                .filter(entity -> request.statusMovimentacao() == null
                        || request.statusMovimentacao().equals(entity.getStatusMovimentacao()))
                .filter(entity -> request.dataInicial() == null
                        || !entity.getDataMovimentacao().isBefore(request.dataInicial()))
                .filter(entity -> request.dataFinal() == null
                        || !entity.getDataMovimentacao().isAfter(request.dataFinal()))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtrados.size());

        List<MovimentacaoEstoqueResponseDto> content = start >= filtrados.size()
                ? List.of()
                : filtrados.subList(start, end)
                .stream()
                .map(movimentacaoEstoqueMapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, filtrados.size());
    }

    /**
     * Remove uma movimentação da empresa corrente.
     *
     * @param id identificador da movimentação
     */
    public void deletar(Long id) {
        MovimentacaoEstoque entity = buscarMovimentacao(id);
        movimentacaoEstoqueRepository.delete(entity);
    }

    /**
     * Busca uma movimentação e garante que ela pertence ao tenant corrente.
     *
     * @param id identificador da movimentação
     * @return entidade encontrada
     */
    private MovimentacaoEstoque buscarMovimentacao(Long id) {
        MovimentacaoEstoque entity = movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação de estoque não encontrada: " + id));

        if (!TenantContext.getEmpresaId().equals(entity.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à movimentação fora do tenant.");

        return entity;
    }

    /**
     * Valida se já existe outra movimentação com o mesmo documento de referência
     * para a empresa corrente.
     *
     * @param documentoReferencia documento de referência
     */
    private void validarDocumentoReferenciaDuplicado(String documentoReferencia) {
        if (documentoReferencia == null || documentoReferencia.isBlank())
            return;
        if (movimentacaoEstoqueRepository.existsByDocumentoReferenciaAndEmpresaId(documentoReferencia, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma movimentação com o documento de referência informado neste tenant.");
    }

    /**
     * Verifica se o documento de referência foi alterado no update.
     *
     * @param entity entidade atual
     * @param novoDocumento novo documento de referência
     * @return true se houve alteração
     */
    private boolean documentoReferenciaAlterado(MovimentacaoEstoque entity, String novoDocumento) {
        String documentoAtual = entity.getDocumentoReferencia();

        if (documentoAtual == null && (novoDocumento == null || novoDocumento.isBlank()))
            return false;

        return !Optional.ofNullable(documentoAtual).orElse("")
                .equalsIgnoreCase(Optional.ofNullable(novoDocumento).orElse(""));
    }

    /**
     * Valida a coerência dos dados básicos da movimentação.
     *
     * @param tipoMovimentacao tipo da movimentação
     * @param estoqueOrigemId estoque de origem
     * @param estoqueDestinoId estoque de destino
     */
    private void validarConsistenciaMovimentacao(TipoMovimentacaoEstoque tipoMovimentacao, Long estoqueOrigemId, Long estoqueDestinoId) {
        if (estoqueOrigemId == null || estoqueDestinoId == null)
            throw new IllegalArgumentException("O estoque de origem ou estoque de destino deve ser informado.");
        if (estoqueOrigemId.equals(estoqueDestinoId))
            throw new IllegalArgumentException("O estoque de origem e o estoque de destino não podem ser iguais.");
        if (tipoMovimentacao == null)
            throw new IllegalArgumentException("Tipo de movimentação deve ser informada.");
    }

    /**
     * Valida o intervalo informado para pesquisa por período.
     *
     * @param dataInicial data inicial
     * @param dataFinal data final
     */
    private void validarPeriodo(java.time.LocalDateTime dataInicial, java.time.LocalDateTime dataFinal) {
        if (dataInicial != null && dataFinal != null && dataInicial.isAfter(dataFinal))
            throw new IllegalArgumentException("A data inicial não pode ser maior que a data final.");
    }

}