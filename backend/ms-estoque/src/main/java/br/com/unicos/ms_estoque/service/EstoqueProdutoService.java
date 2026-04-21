package br.com.unicos.ms_estoque.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoResponseDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoSearchRequestDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoUpdateRequestDto;
import br.com.unicos.ms_estoque.mapper.EstoqueProdutoMapper;
import br.com.unicos.ms_estoque.model.EstoqueProduto;
import br.com.unicos.ms_estoque.repository.EstoqueProdutoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Service responsável pelas regras de negócio e operações do agregado {@link EstoqueProduto}.
 */
@Service
@Transactional
public class EstoqueProdutoService extends BaseTenantService<EstoqueProduto, Long> {

    private static final String CIRCUIT_BREAKER_NAME = "estoque-produto-admin";
    private static final String FALLBACK_MESSAGE = "Serviço de saldo de produtos em estoque temporariamente indisponível.";
    private static final String DUPLICATE_MESSAGE =
            "Já existe um registro de produto para o estoque informado neste tenant.";

    private final EstoqueProdutoRepository estoqueProdutoRepository;
    private final EstoqueProdutoMapper estoqueProdutoMapper;

    /**
     * Construtor da service de estoque produto.
     *
     * @param estoqueProdutoRepository repositório de estoque produto
     * @param estoqueProdutoMapper mapper de conversão entre entidade e DTOs
     */
    public EstoqueProdutoService(
            EstoqueProdutoRepository estoqueProdutoRepository,
            EstoqueProdutoMapper estoqueProdutoMapper
    ) {
        super(estoqueProdutoRepository);
        this.estoqueProdutoRepository = estoqueProdutoRepository;
        this.estoqueProdutoMapper = estoqueProdutoMapper;
    }

    /**
     * Cria um novo saldo de produto em estoque para a empresa corrente.
     *
     * @param request dados de criação
     * @return registro criado
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdmin")
    public EstoqueProdutoResponseDto salvar(EstoqueProdutoCreateRequestDto request) {
        validarDuplicidade(request.estoqueId(), request.produtoId());
        validarQuantidades(
                request.quantidadeAtual(),
                request.quantidadeReservada(),
                request.quantidadeDisponivel()
        );

        EstoqueProduto entity = estoqueProdutoMapper.toEntity(request);
        entity.setEmpresaId(obterEmpresaId());

        return estoqueProdutoMapper.toResponse(save(entity));
    }

    /**
     * Atualiza um saldo de produto em estoque existente.
     *
     * @param id identificador do registro
     * @param request dados de atualização
     * @return registro atualizado
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminIdReq")
    public EstoqueProdutoResponseDto atualizar(Long id, EstoqueProdutoUpdateRequestDto request) {
        EstoqueProduto entity = buscarEstoqueProduto(id);

        if (chaveLogicaAlterada(entity, request.estoqueId(), request.produtoId())) {
            validarDuplicidade(request.estoqueId(), request.produtoId());
        }

        validarQuantidades(
                request.quantidadeAtual(),
                request.quantidadeReservada(),
                request.quantidadeDisponivel()
        );

        estoqueProdutoMapper.updateEntity(request, entity);

        return estoqueProdutoMapper.toResponse(save(entity));
    }

    /**
     * Busca um registro por identificador.
     *
     * @param id identificador do registro
     * @return registro encontrado
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminId")
    public EstoqueProdutoResponseDto buscarPorId(Long id) {
        return estoqueProdutoMapper.toResponse(buscarEstoqueProduto(id));
    }

    /**
     * Lista todos os registros da empresa corrente de forma paginada.
     *
     * @param pageable paginação
     * @return página de registros
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminPage")
    public Page<EstoqueProdutoResponseDto> listar(Pageable pageable) {
        return findAllByEmpresaId(obterEmpresaId(), pageable)
                .map(estoqueProdutoMapper::toResponse);
    }

    /**
     * Lista os produtos de um estoque.
     *
     * @param estoqueId identificador do estoque
     * @param pageable paginação
     * @return página de registros
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminEstoquePage")
    public Page<EstoqueProdutoResponseDto> listarPorEstoque(Long estoqueId, Pageable pageable) {
        return estoqueProdutoRepository
                .findByEstoqueIdAndEmpresaId(estoqueId, obterEmpresaId(), pageable)
                .map(estoqueProdutoMapper::toResponse);
    }

    /**
     * Lista as ocorrências de um produto em todos os estoques da empresa corrente.
     *
     * @param produtoId identificador do produto
     * @param pageable paginação
     * @return página de registros
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminProdutoPage")
    public Page<EstoqueProdutoResponseDto> listarPorProduto(Long produtoId, Pageable pageable) {
        return estoqueProdutoRepository
                .findByProdutoIdAndEmpresaId(produtoId, obterEmpresaId(), pageable)
                .map(estoqueProdutoMapper::toResponse);
    }

    /**
     * Busca o saldo de um produto em um estoque específico.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @return registro encontrado
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminEstoqueProduto")
    public EstoqueProdutoResponseDto buscarPorEstoqueEProduto(Long estoqueId, Long produtoId) {
        EstoqueProduto entity = estoqueProdutoRepository
                .findByEstoqueIdAndProdutoIdAndEmpresaId(estoqueId, produtoId, obterEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Saldo não encontrado para estoque " + estoqueId + " e produto " + produtoId
                ));

        return estoqueProdutoMapper.toResponse(entity);
    }

    /**
     * Pesquisa registros com base nos filtros informados.
     *
     * <p>
     * Como o repositório atual não possui consultas dinâmicas, os filtros são aplicados
     * em memória sobre os registros do tenant corrente.
     * </p>
     *
     * @param request filtros da pesquisa
     * @param pageable paginação
     * @return página filtrada de registros
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminSearch")
    public Page<EstoqueProdutoResponseDto> pesquisar(
            EstoqueProdutoSearchRequestDto request,
            Pageable pageable
    ) {
        if (request == null) {
            return listar(pageable);
        }

        List<EstoqueProduto> filtrados = findAllByEmpresaId(obterEmpresaId(), Pageable.unpaged())
                .stream()
                .filter(entity -> request.estoqueId() == null
                        || request.estoqueId().equals(entity.getEstoqueId()))
                .filter(entity -> request.produtoId() == null
                        || request.produtoId().equals(entity.getProdutoId()))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtrados.size());

        List<EstoqueProdutoResponseDto> content = start >= filtrados.size()
                ? List.of()
                : filtrados.subList(start, end)
                .stream()
                .map(estoqueProdutoMapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, filtrados.size());
    }

    /**
     * Remove um registro da empresa corrente.
     *
     * @param id identificador do registro
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        EstoqueProduto entity = buscarEstoqueProduto(id);
        estoqueProdutoRepository.delete(entity);
    }

    /**
     * Ajusta o saldo de um produto em estoque com lock pessimista.
     *
     * <p>
     * Método útil para operações de entrada, saída, reserva e transferência.
     * </p>
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @param quantidadeAtual nova quantidade atual
     * @param quantidadeReservada nova quantidade reservada
     * @return registro atualizado
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackAdminAjuste")
    public EstoqueProdutoResponseDto ajustarSaldoComLock(
            Long estoqueId,
            Long produtoId,
            BigDecimal quantidadeAtual,
            BigDecimal quantidadeReservada
    ) {
        EstoqueProduto entity = estoqueProdutoRepository
                .findWithLockByEstoqueIdAndProdutoIdAndEmpresaId(estoqueId, produtoId, obterEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Saldo não encontrado para estoque " + estoqueId + " e produto " + produtoId
                ));

        BigDecimal quantidadeDisponivel = quantidadeAtual.subtract(quantidadeReservada);

        validarQuantidades(quantidadeAtual, quantidadeReservada, quantidadeDisponivel);

        entity.setQuantidadeAtual(quantidadeAtual);
        entity.setQuantidadeReservada(quantidadeReservada);
        entity.setQuantidadeDisponivel(quantidadeDisponivel);

        return estoqueProdutoMapper.toResponse(save(entity));
    }

    /**
     * Fallback para operações com request simples.
     *
     * @param req request recebido
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private EstoqueProdutoResponseDto fallbackAdmin(Object req, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para operações com identificador.
     *
     * @param id identificador recebido
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private EstoqueProdutoResponseDto fallbackAdminId(Long id, Throwable ex) {
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
    private EstoqueProdutoResponseDto fallbackAdminIdReq(Long id, Object req, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para listagem paginada genérica.
     *
     * @param pageable paginação recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private Page<EstoqueProdutoResponseDto> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para listagem paginada por estoque.
     *
     * @param estoqueId identificador recebido
     * @param pageable paginação recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private Page<EstoqueProdutoResponseDto> fallbackAdminEstoquePage(
            Long estoqueId,
            Pageable pageable,
            Throwable ex
    ) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para listagem paginada por produto.
     *
     * @param produtoId identificador recebido
     * @param pageable paginação recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private Page<EstoqueProdutoResponseDto> fallbackAdminProdutoPage(
            Long produtoId,
            Pageable pageable,
            Throwable ex
    ) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para busca por estoque e produto.
     *
     * @param estoqueId identificador recebido
     * @param produtoId identificador recebido
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private EstoqueProdutoResponseDto fallbackAdminEstoqueProduto(
            Long estoqueId,
            Long produtoId,
            Throwable ex
    ) {
        throw indisponibilidade(ex);
    }

    /**
     * Fallback para pesquisa paginada.
     *
     * @param request filtros recebidos
     * @param pageable paginação recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private Page<EstoqueProdutoResponseDto> fallbackAdminSearch(
            EstoqueProdutoSearchRequestDto request,
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
     * Fallback para ajuste de saldo com lock.
     *
     * @param estoqueId identificador recebido
     * @param produtoId identificador recebido
     * @param quantidadeAtual quantidade recebida
     * @param quantidadeReservada quantidade recebida
     * @param ex exceção original
     * @return nunca retorna com sucesso
     */
    private EstoqueProdutoResponseDto fallbackAdminAjuste(
            Long estoqueId,
            Long produtoId,
            BigDecimal quantidadeAtual,
            BigDecimal quantidadeReservada,
            Throwable ex
    ) {
        throw indisponibilidade(ex);
    }

    /**
     * Busca um registro e garante que ele pertence ao tenant corrente.
     *
     * @param id identificador do registro
     * @return entidade encontrada
     */
    private EstoqueProduto buscarEstoqueProduto(Long id) {
        EstoqueProduto entity = estoqueProdutoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EstoqueProduto não encontrado: " + id));

        if (!obterEmpresaId().equals(entity.getEmpresaId())) {
            throw new AccessDeniedException("Acesso negado ao saldo de estoque fora do tenant.");
        }

        return entity;
    }

    /**
     * Valida se já existe outro registro para o mesmo estoque e produto.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     */
    private void validarDuplicidade(Long estoqueId, Long produtoId) {
        if (estoqueProdutoRepository.existsByEstoqueIdAndProdutoIdAndEmpresaId(
                estoqueId,
                produtoId,
                obterEmpresaId()
        )) {
            throw new IllegalArgumentException(DUPLICATE_MESSAGE);
        }
    }

    /**
     * Verifica se a chave lógica foi alterada no update.
     *
     * @param entity entidade atual
     * @param estoqueId novo estoque
     * @param produtoId novo produto
     * @return true se houve alteração
     */
    private boolean chaveLogicaAlterada(EstoqueProduto entity, Long estoqueId, Long produtoId) {
        return !Objects.equals(entity.getEstoqueId(), estoqueId)
                || !Objects.equals(entity.getProdutoId(), produtoId);
    }

    /**
     * Valida consistência das quantidades do saldo.
     *
     * @param quantidadeAtual quantidade atual
     * @param quantidadeReservada quantidade reservada
     * @param quantidadeDisponivel quantidade disponível
     */
    private void validarQuantidades(
            BigDecimal quantidadeAtual,
            BigDecimal quantidadeReservada,
            BigDecimal quantidadeDisponivel
    ) {
        if (quantidadeAtual == null || quantidadeAtual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("A quantidade atual não pode ser negativa.");
        }

        if (quantidadeReservada == null || quantidadeReservada.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("A quantidade reservada não pode ser negativa.");
        }

        if (quantidadeDisponivel == null || quantidadeDisponivel.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("A quantidade disponível não pode ser negativa.");
        }

        if (quantidadeReservada.compareTo(quantidadeAtual) > 0) {
            throw new IllegalArgumentException("A quantidade reservada não pode ser maior que a quantidade atual.");
        }

        BigDecimal esperado = quantidadeAtual.subtract(quantidadeReservada);
        if (esperado.compareTo(quantidadeDisponivel) != 0) {
            throw new IllegalArgumentException(
                    "A quantidade disponível deve ser igual à quantidade atual menos a quantidade reservada."
            );
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