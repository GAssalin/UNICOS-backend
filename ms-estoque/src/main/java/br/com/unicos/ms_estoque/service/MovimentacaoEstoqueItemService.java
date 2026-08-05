package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemCreateRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemResponseDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemSearchRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemUpdateRequestDto;
import br.com.unicos.ms_estoque.mapper.MovimentacaoEstoqueItemMapper;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoqueItem;
import br.com.unicos.ms_estoque.repository.MovimentacaoEstoqueItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service responsável pelas regras de negócio e operações do agregado {@link MovimentacaoEstoqueItem}.
 */
@Service
@Transactional
public class MovimentacaoEstoqueItemService extends BaseTenantService<MovimentacaoEstoqueItem, Long> {

    private final MovimentacaoEstoqueItemRepository movimentacaoEstoqueItemRepository;
    private final MovimentacaoEstoqueItemMapper movimentacaoEstoqueItemMapper;

    /**
     * Construtor da service de item de movimentação de estoque.
     *
     * @param movimentacaoEstoqueItemRepository repositório do item
     * @param movimentacaoEstoqueItemMapper mapper de conversão entre entidade e DTOs
     */
    public MovimentacaoEstoqueItemService(MovimentacaoEstoqueItemRepository movimentacaoEstoqueItemRepository, MovimentacaoEstoqueItemMapper movimentacaoEstoqueItemMapper) {
        super(movimentacaoEstoqueItemRepository);
        this.movimentacaoEstoqueItemRepository = movimentacaoEstoqueItemRepository;
        this.movimentacaoEstoqueItemMapper = movimentacaoEstoqueItemMapper;
    }

    /**
     * Cria um novo item de movimentação para a empresa corrente.
     *
     * @param request dados de criação do item
     * @return item criado
     */
    public MovimentacaoEstoqueItemResponseDto salvar(MovimentacaoEstoqueItemCreateRequestDto request) {
        validarItemDuplicado(request.movimentacaoId(), request.produtoId());
        validarQuantidade(request.quantidade());
        validarValorUnitario(request.valorUnitario());

        MovimentacaoEstoqueItem entity = movimentacaoEstoqueItemMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());

        return movimentacaoEstoqueItemMapper.toResponse(save(entity));
    }

    /**
     * Atualiza um item de movimentação existente.
     *
     * @param id identificador do item
     * @param request dados de atualização
     * @return item atualizado
     */
    public MovimentacaoEstoqueItemResponseDto atualizar(Long id, MovimentacaoEstoqueItemUpdateRequestDto request) {
        MovimentacaoEstoqueItem entity = buscarItem(id);

        if (chaveLogicaAlterada(entity, request.movimentacaoId(), request.produtoId()))
            validarItemDuplicado(request.movimentacaoId(), request.produtoId());

        validarQuantidade(request.quantidade());
        validarValorUnitario(request.valorUnitario());

        movimentacaoEstoqueItemMapper.updateEntity(request, entity);

        return movimentacaoEstoqueItemMapper.toResponse(save(entity));
    }

    /**
     * Busca um item por identificador.
     *
     * @param id identificador do item
     * @return item encontrado
     */
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueItemResponseDto buscarPorId(Long id) {
        return movimentacaoEstoqueItemMapper.toResponse(buscarItem(id));
    }

    /**
     * Lista todos os itens da empresa corrente de forma paginada.
     *
     * @param pageable paginação
     * @return página de itens
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueItemResponseDto> listar(Pageable pageable) {
        return findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueItemMapper::toResponse);
    }

    /**
     * Lista os itens de uma movimentação.
     *
     * @param movimentacaoId identificador da movimentação
     * @return lista de itens
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueItemResponseDto> listarPorMovimentacao(Long movimentacaoId) {
        return movimentacaoEstoqueItemRepository
                .findByMovimentacaoIdAndEmpresaId(movimentacaoId, TenantContext.getEmpresaId())
                .stream()
                .map(movimentacaoEstoqueItemMapper::toResponse)
                .toList();
    }

    /**
     * Lista os itens por produto.
     *
     * @param produtoId identificador do produto
     * @param pageable paginação
     * @return página de itens
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueItemResponseDto> listarPorProduto(Long produtoId, Pageable pageable) {
        return movimentacaoEstoqueItemRepository
                .findByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId(), pageable)
                .map(movimentacaoEstoqueItemMapper::toResponse);
    }

    /**
     * Busca um item por movimentação e produto.
     *
     * @param movimentacaoId identificador da movimentação
     * @param produtoId identificador do produto
     * @return item encontrado
     */
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueItemResponseDto buscarPorMovimentacaoEProduto(Long movimentacaoId, Long produtoId) {
        MovimentacaoEstoqueItem entity = movimentacaoEstoqueItemRepository
                .findByMovimentacaoIdAndProdutoIdAndEmpresaId(movimentacaoId, produtoId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de movimentação não encontrado para movimentação " + movimentacaoId
                                + " e produto " + produtoId
                ));

        return movimentacaoEstoqueItemMapper.toResponse(entity);
    }

    /**
     * Pesquisa itens com base nos filtros informados.
     *
     * <p>
     * Como o repositório atual não possui consultas dinâmicas, os filtros são aplicados
     * em memória sobre os registros do tenant corrente.
     * </p>
     *
     * @param request filtros da pesquisa
     * @param pageable paginação
     * @return página filtrada de itens
     */
    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueItemResponseDto> pesquisar(MovimentacaoEstoqueItemSearchRequestDto request, Pageable pageable) {
        if (request == null)
            return listar(pageable);

        List<MovimentacaoEstoqueItem> filtrados = findAllByEmpresaId(TenantContext.getEmpresaId(), Pageable.unpaged())
                .stream()
                .filter(entity -> request.movimentacaoId() == null
                        || request.movimentacaoId().equals(entity.getMovimentacaoId()))
                .filter(entity -> request.produtoId() == null
                        || request.produtoId().equals(entity.getProdutoId()))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtrados.size());

        List<MovimentacaoEstoqueItemResponseDto> content = start >= filtrados.size()
                ? List.of()
                : filtrados.subList(start, end)
                .stream()
                .map(movimentacaoEstoqueItemMapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, filtrados.size());
    }

    /**
     * Remove um item da empresa corrente.
     *
     * @param id identificador do item
     */
    public void deletar(Long id) {
        MovimentacaoEstoqueItem entity = buscarItem(id);
        movimentacaoEstoqueItemRepository.delete(entity);
    }

    /**
     * Busca um item e garante que ele pertence ao tenant corrente.
     *
     * @param id identificador do item
     * @return entidade encontrada
     */
    private MovimentacaoEstoqueItem buscarItem(Long id) {
        MovimentacaoEstoqueItem entity = movimentacaoEstoqueItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de movimentação de estoque não encontrado: " + id));

        if (!TenantContext.getEmpresaId().equals(entity.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de movimentação fora do tenant.");

        return entity;
    }

    /**
     * Valida se já existe item para a mesma movimentação e produto.
     *
     * @param movimentacaoId identificador da movimentação
     * @param produtoId identificador do produto
     */
    private void validarItemDuplicado(Long movimentacaoId, Long produtoId) {
        if (movimentacaoEstoqueItemRepository.existsByMovimentacaoIdAndProdutoIdAndEmpresaId(movimentacaoId, produtoId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um item para o produto informado nesta movimentação e tenant.");
    }

    /**
     * Verifica se a chave lógica do item foi alterada no update.
     *
     * @param entity entidade atual
     * @param movimentacaoId nova movimentação
     * @param produtoId novo produto
     * @return true se houve alteração
     */
    private boolean chaveLogicaAlterada(MovimentacaoEstoqueItem entity, Long movimentacaoId, Long produtoId) {
        return !entity.getMovimentacaoId().equals(movimentacaoId)
                || !entity.getProdutoId().equals(produtoId);
    }

    /**
     * Valida a quantidade informada.
     *
     * @param quantidade quantidade do item
     */
    private void validarQuantidade(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
    }

    /**
     * Valida o valor unitário informado.
     *
     * @param valorUnitario valor unitário do item
     */
    private void validarValorUnitario(BigDecimal valorUnitario) {
        if (valorUnitario != null && valorUnitario.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("O valor unitário não pode ser negativo.");
    }

}