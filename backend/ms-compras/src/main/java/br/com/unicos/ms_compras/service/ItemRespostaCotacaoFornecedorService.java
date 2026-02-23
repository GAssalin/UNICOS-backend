package br.com.unicos.ms_compras.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_compras.dto.ItemRespostaCotacaoFornecedorDto;
import br.com.unicos.ms_compras.mapper.ItemRespostaCotacaoFornecedorMapper;
import br.com.unicos.ms_compras.model.ItemCotacao;
import br.com.unicos.ms_compras.model.ItemRespostaCotacaoFornecedor;
import br.com.unicos.ms_compras.model.RespostaCotacaoFornecedor;
import br.com.unicos.ms_compras.repository.ItemCotacaoRepository;
import br.com.unicos.ms_compras.repository.ItemRespostaCotacaoFornecedorRepository;
import br.com.unicos.ms_compras.repository.RespostaCotacaoFornecedorRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

/**
 * Service responsável por regras de negócio e operações do agregado {@link ItemRespostaCotacaoFornecedor}.
 *
 * <p>
 * Padrões UniCoS:
 * - Relacionamentos com {@link RespostaCotacaoFornecedor} e {@link ItemCotacao} resolvidos no service
 * - Proteção tenant usando métodos tenant-aware
 * - Regra: 1 proposta por (respostaCotacaoFornecedor, itemCotacao) no tenant
 * - Regra: totalItem = (quantidade do itemCotacao) * precoUnitario - descontoItem
 * </p>
 */
@Service
@Transactional
public class ItemRespostaCotacaoFornecedorService extends BaseTenantService<ItemRespostaCotacaoFornecedor, Long> {

    private final ItemRespostaCotacaoFornecedorRepository itemRespostaRepository;
    private final RespostaCotacaoFornecedorRepository respostaRepository;
    private final ItemCotacaoRepository itemCotacaoRepository;
    private final ItemRespostaCotacaoFornecedorMapper itemRespostaMapper;

    public ItemRespostaCotacaoFornecedorService(
            ItemRespostaCotacaoFornecedorRepository itemRespostaRepository,
            RespostaCotacaoFornecedorRepository respostaRepository,
            ItemCotacaoRepository itemCotacaoRepository,
            ItemRespostaCotacaoFornecedorMapper itemRespostaMapper
    ) {
        super(itemRespostaRepository);
        this.itemRespostaRepository = itemRespostaRepository;
        this.respostaRepository = respostaRepository;
        this.itemCotacaoRepository = itemCotacaoRepository;
        this.itemRespostaMapper = itemRespostaMapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdmin")
    public ItemRespostaCotacaoFornecedorDto salvar(ItemRespostaCotacaoFornecedorDto request) {
        if (request.respostaCotacaoFornecedorId() == null)
            throw new IllegalArgumentException("respostaCotacaoFornecedorId é obrigatório.");
        if (request.itemCotacaoId() == null)
            throw new IllegalArgumentException("itemCotacaoId é obrigatório.");

        RespostaCotacaoFornecedor resposta = buscarResposta(request.respostaCotacaoFornecedorId());
        ItemCotacao itemCotacao = buscarItemCotacao(request.itemCotacaoId());

        validarDuplicidadeAoSalvar(resposta.getId(), itemCotacao.getId());

        ItemRespostaCotacaoFornecedor entity = itemRespostaMapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setRespostaCotacaoFornecedor(resposta);
        entity.setItemCotacao(itemCotacao);

        normalizarTotais(entity);

        return itemRespostaMapper.toResponse(itemRespostaRepository.save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminIdReq")
    public ItemRespostaCotacaoFornecedorDto atualizar(Long id, ItemRespostaCotacaoFornecedorDto request) {
        ItemRespostaCotacaoFornecedor entity = buscarItemResposta(id);

        // Não permite trocar respostaCotacaoFornecedor por este endpoint
        if (request.respostaCotacaoFornecedorId() != null
                && entity.getRespostaCotacaoFornecedor() != null
                && !entity.getRespostaCotacaoFornecedor().getId().equals(request.respostaCotacaoFornecedorId())) {
            throw new IllegalArgumentException("Não é permitido alterar respostaCotacaoFornecedorId deste item por este endpoint.");
        }

        // Não permite trocar itemCotacao por este endpoint
        if (request.itemCotacaoId() != null
                && entity.getItemCotacao() != null
                && !entity.getItemCotacao().getId().equals(request.itemCotacaoId())) {
            throw new IllegalArgumentException("Não é permitido alterar itemCotacaoId deste item por este endpoint.");
        }

        itemRespostaMapper.updateEntity(request, entity);

        normalizarTotais(entity);

        return itemRespostaMapper.toResponse(itemRespostaRepository.save(entity));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminId")
    public ItemRespostaCotacaoFornecedorDto buscarPorId(Long id) {
        return itemRespostaMapper.toResponse(buscarItemResposta(id));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminByRespostaItem")
    public ItemRespostaCotacaoFornecedorDto buscarPorRespostaEItemCotacao(Long respostaCotacaoFornecedorId, Long itemCotacaoId) {
        ItemRespostaCotacaoFornecedor entity = itemRespostaRepository
                .findByRespostaCotacaoFornecedorIdAndItemCotacaoIdAndEmpresaId(
                        respostaCotacaoFornecedorId,
                        itemCotacaoId,
                        TenantContext.getEmpresaId()
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de resposta não encontrado para respostaCotacaoFornecedorId="
                                + respostaCotacaoFornecedorId + " e itemCotacaoId=" + itemCotacaoId
                ));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de resposta fora do tenant.");

        return itemRespostaMapper.toResponse(entity);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ItemRespostaCotacaoFornecedorDto> listar(Pageable pageable) {
        return itemRespostaRepository
                .findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(itemRespostaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminPageResposta")
    public Page<ItemRespostaCotacaoFornecedorDto> listarPorResposta(Long respostaCotacaoFornecedorId, Pageable pageable) {
        buscarResposta(respostaCotacaoFornecedorId);

        return itemRespostaRepository
                .findByRespostaCotacaoFornecedorIdAndEmpresaId(respostaCotacaoFornecedorId, TenantContext.getEmpresaId(), pageable)
                .map(itemRespostaMapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        itemRespostaRepository.delete(buscarItemResposta(id));
    }

    @CircuitBreaker(name = "compras-item-resposta-cotacao-fornecedor-admin", fallbackMethod = "fallbackAdminVoidResposta")
    public void deletarPorResposta(Long respostaCotacaoFornecedorId) {
        buscarResposta(respostaCotacaoFornecedorId);

        itemRespostaRepository.deleteByRespostaCotacaoFornecedorIdAndEmpresaId(
                respostaCotacaoFornecedorId,
                TenantContext.getEmpresaId()
        );
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ItemRespostaCotacaoFornecedorDto fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private ItemRespostaCotacaoFornecedorDto fallbackAdminId(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private ItemRespostaCotacaoFornecedorDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private ItemRespostaCotacaoFornecedorDto fallbackAdminByRespostaItem(Long respostaCotacaoFornecedorId, Long itemCotacaoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private Page<ItemRespostaCotacaoFornecedorDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private Page<ItemRespostaCotacaoFornecedorDto> fallbackAdminPageResposta(Long respostaCotacaoFornecedorId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    private void fallbackAdminVoidResposta(Long respostaCotacaoFornecedorId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de itens de resposta de cotação temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ItemRespostaCotacaoFornecedor buscarItemResposta(Long id) {
        ItemRespostaCotacaoFornecedor entity = itemRespostaRepository
                .findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("ItemRespostaCotacaoFornecedor não encontrado: " + id));

        if (!entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de resposta fora do tenant.");

        return entity;
    }

    private RespostaCotacaoFornecedor buscarResposta(Long id) {
        // Ideal: respostaRepository.findByIdAndEmpresaId(...)
        RespostaCotacaoFornecedor entity = respostaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RespostaCotacaoFornecedor não encontrada: " + id));

        if (entity.getEmpresaId() != null && !entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado à resposta fora do tenant.");

        return entity;
    }

    private ItemCotacao buscarItemCotacao(Long id) {
        // Ideal: itemCotacaoRepository.findByIdAndEmpresaId(...)
        ItemCotacao entity = itemCotacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ItemCotacao não encontrado: " + id));

        if (entity.getEmpresaId() != null && !entity.getEmpresaId().equals(TenantContext.getEmpresaId()))
            throw new AccessDeniedException("Acesso negado ao item de cotação fora do tenant.");

        return entity;
    }

    private void validarDuplicidadeAoSalvar(Long respostaCotacaoFornecedorId, Long itemCotacaoId) {
        if (itemRespostaRepository.existsByRespostaCotacaoFornecedorIdAndItemCotacaoIdAndEmpresaId(
                respostaCotacaoFornecedorId,
                itemCotacaoId,
                TenantContext.getEmpresaId()
        )) {
            throw new IllegalArgumentException("Já existe proposta para este item nesta resposta (tenant).");
        }
    }

    private void normalizarTotais(ItemRespostaCotacaoFornecedor entity) {
        if (entity.getPrecoUnitario() == null)
            throw new IllegalArgumentException("precoUnitario é obrigatório.");
        if (entity.getDescontoItem() == null)
            // mantém compatível com @NotNull do model
            entity.setDescontoItem(BigDecimal.ZERO);
        if (entity.getPrecoUnitario().signum() < 0 || entity.getDescontoItem().signum() < 0)
            throw new IllegalArgumentException("precoUnitario e descontoItem não podem ser negativos.");
        if (entity.getItemCotacao() == null || entity.getItemCotacao().getQuantidade() == null)
            throw new IllegalStateException("ItemCotacao e quantidade do itemCotacao são obrigatórios para calcular totalItem.");

        BigDecimal total = entity.getItemCotacao()
                .getQuantidade()
                .multiply(entity.getPrecoUnitario())
                .subtract(entity.getDescontoItem());

        if (total.signum() < 0)
            throw new IllegalArgumentException("descontoItem não pode tornar totalItem negativo.");

        entity.setTotalItem(total);
    }
}