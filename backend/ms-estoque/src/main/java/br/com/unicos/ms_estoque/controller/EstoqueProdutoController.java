package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoResponseDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoSearchRequestDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoUpdateRequestDto;
import br.com.unicos.ms_estoque.service.EstoqueProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller responsável pelos endpoints de gerenciamento de saldo de produtos em estoque.
 */
@RestController
@RequestMapping("/api/estoques-produtos")
@RequiredArgsConstructor
public class EstoqueProdutoController {

    private final EstoqueProdutoService estoqueProdutoService;

    /**
     * Cadastra um novo saldo de produto em estoque.
     *
     * @param request dados para criação do saldo
     * @return saldo criado
     */
    @PostMapping
    public ResponseEntity<EstoqueProdutoResponseDto> criar(
            @Valid @RequestBody EstoqueProdutoCreateRequestDto request
    ) {
        EstoqueProdutoResponseDto response = estoqueProdutoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca um saldo de produto em estoque pelo ID.
     *
     * @param id identificador do registro
     * @return saldo encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueProdutoResponseDto> buscarPorId(@PathVariable Long id) {
        EstoqueProdutoResponseDto response = estoqueProdutoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os saldos de produtos em estoque de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página de saldos
     */
    @GetMapping
    public ResponseEntity<Page<EstoqueProdutoResponseDto>> listar(Pageable pageable) {
        Page<EstoqueProdutoResponseDto> response = estoqueProdutoService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista os saldos de produtos de um estoque específico.
     *
     * @param estoqueId identificador do estoque
     * @param pageable parâmetros de paginação
     * @return página de saldos
     */
    @GetMapping("/estoque/{estoqueId}")
    public ResponseEntity<Page<EstoqueProdutoResponseDto>> listarPorEstoque(
            @PathVariable Long estoqueId,
            Pageable pageable
    ) {
        Page<EstoqueProdutoResponseDto> response =
                estoqueProdutoService.listarPorEstoque(estoqueId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista as ocorrências de um produto em todos os estoques.
     *
     * @param produtoId identificador do produto
     * @param pageable parâmetros de paginação
     * @return página de saldos
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<Page<EstoqueProdutoResponseDto>> listarPorProduto(
            @PathVariable Long produtoId,
            Pageable pageable
    ) {
        Page<EstoqueProdutoResponseDto> response =
                estoqueProdutoService.listarPorProduto(produtoId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca o saldo de um produto em um estoque específico.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @return saldo encontrado
     */
    @GetMapping("/estoque/{estoqueId}/produto/{produtoId}")
    public ResponseEntity<EstoqueProdutoResponseDto> buscarPorEstoqueEProduto(
            @PathVariable Long estoqueId,
            @PathVariable Long produtoId
    ) {
        EstoqueProdutoResponseDto response =
                estoqueProdutoService.buscarPorEstoqueEProduto(estoqueId, produtoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Pesquisa saldos com base nos filtros informados.
     *
     * @param estoqueId identificador do estoque para filtro
     * @param produtoId identificador do produto para filtro
     * @param pageable parâmetros de paginação
     * @return página de saldos filtrados
     */
    @GetMapping("/pesquisa")
    public ResponseEntity<Page<EstoqueProdutoResponseDto>> pesquisar(
            @RequestParam(required = false) Long estoqueId,
            @RequestParam(required = false) Long produtoId,
            Pageable pageable
    ) {
        EstoqueProdutoSearchRequestDto request =
                new EstoqueProdutoSearchRequestDto(estoqueId, produtoId);

        Page<EstoqueProdutoResponseDto> response =
                estoqueProdutoService.pesquisar(request, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um saldo de produto em estoque.
     *
     * @param id identificador do registro
     * @param request dados para atualização
     * @return saldo atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstoqueProdutoResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstoqueProdutoUpdateRequestDto request
    ) {
        EstoqueProdutoResponseDto response = estoqueProdutoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um saldo de produto em estoque.
     *
     * @param id identificador do registro
     * @return resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ajusta o saldo de um produto em estoque com lock pessimista.
     *
     * @param estoqueId identificador do estoque
     * @param produtoId identificador do produto
     * @param quantidadeAtual nova quantidade atual
     * @param quantidadeReservada nova quantidade reservada
     * @return saldo atualizado
     */
    @PatchMapping("/estoque/{estoqueId}/produto/{produtoId}/ajuste")
    public ResponseEntity<EstoqueProdutoResponseDto> ajustarSaldoComLock(
            @PathVariable Long estoqueId,
            @PathVariable Long produtoId,
            @RequestParam BigDecimal quantidadeAtual,
            @RequestParam BigDecimal quantidadeReservada
    ) {
        EstoqueProdutoResponseDto response = estoqueProdutoService.ajustarSaldoComLock(
                estoqueId,
                produtoId,
                quantidadeAtual,
                quantidadeReservada
        );
        return ResponseEntity.ok(response);
    }
}