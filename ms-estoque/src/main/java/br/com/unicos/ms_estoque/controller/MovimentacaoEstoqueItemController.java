package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemCreateRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemResponseDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemSearchRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemUpdateRequestDto;
import br.com.unicos.ms_estoque.service.MovimentacaoEstoqueItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelos endpoints de gerenciamento de itens de movimentação de estoque.
 */
@RestController
@RequestMapping("/v1/movimentacoes-estoque-itens")
@RequiredArgsConstructor
public class MovimentacaoEstoqueItemController {

    private final MovimentacaoEstoqueItemService movimentacaoEstoqueItemService;

    /**
     * Cadastra um novo item de movimentação de estoque.
     *
     * @param request dados para criação do item
     * @return item criado
     */
    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueItemResponseDto> criar(
            @Valid @RequestBody MovimentacaoEstoqueItemCreateRequestDto request
    ) {
        MovimentacaoEstoqueItemResponseDto response = movimentacaoEstoqueItemService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca um item pelo ID.
     *
     * @param id identificador do item
     * @return item encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueItemResponseDto> buscarPorId(@PathVariable Long id) {
        MovimentacaoEstoqueItemResponseDto response = movimentacaoEstoqueItemService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os itens de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página de itens
     */
    @GetMapping
    public ResponseEntity<Page<MovimentacaoEstoqueItemResponseDto>> listar(Pageable pageable) {
        Page<MovimentacaoEstoqueItemResponseDto> response = movimentacaoEstoqueItemService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista os itens de uma movimentação.
     *
     * @param movimentacaoId identificador da movimentação
     * @return lista de itens da movimentação
     */
    @GetMapping("/movimentacao/{movimentacaoId}")
    public ResponseEntity<List<MovimentacaoEstoqueItemResponseDto>> listarPorMovimentacao(
            @PathVariable Long movimentacaoId
    ) {
        List<MovimentacaoEstoqueItemResponseDto> response =
                movimentacaoEstoqueItemService.listarPorMovimentacao(movimentacaoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista os itens por produto.
     *
     * @param produtoId identificador do produto
     * @param pageable parâmetros de paginação
     * @return página de itens
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<Page<MovimentacaoEstoqueItemResponseDto>> listarPorProduto(
            @PathVariable Long produtoId,
            Pageable pageable
    ) {
        Page<MovimentacaoEstoqueItemResponseDto> response =
                movimentacaoEstoqueItemService.listarPorProduto(produtoId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um item por movimentação e produto.
     *
     * @param movimentacaoId identificador da movimentação
     * @param produtoId identificador do produto
     * @return item encontrado
     */
    @GetMapping("/movimentacao/{movimentacaoId}/produto/{produtoId}")
    public ResponseEntity<MovimentacaoEstoqueItemResponseDto> buscarPorMovimentacaoEProduto(
            @PathVariable Long movimentacaoId,
            @PathVariable Long produtoId
    ) {
        MovimentacaoEstoqueItemResponseDto response =
                movimentacaoEstoqueItemService.buscarPorMovimentacaoEProduto(movimentacaoId, produtoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Pesquisa itens com base nos filtros informados.
     *
     * @param movimentacaoId identificador da movimentação para filtro
     * @param produtoId identificador do produto para filtro
     * @param pageable parâmetros de paginação
     * @return página de itens filtrados
     */
    @GetMapping("/pesquisa")
    public ResponseEntity<Page<MovimentacaoEstoqueItemResponseDto>> pesquisar(
            @RequestParam(required = false) Long movimentacaoId,
            @RequestParam(required = false) Long produtoId,
            Pageable pageable
    ) {
        MovimentacaoEstoqueItemSearchRequestDto request =
                new MovimentacaoEstoqueItemSearchRequestDto(movimentacaoId, produtoId);

        Page<MovimentacaoEstoqueItemResponseDto> response =
                movimentacaoEstoqueItemService.pesquisar(request, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza os dados de um item de movimentação.
     *
     * @param id identificador do item
     * @param request dados para atualização
     * @return item atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueItemResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoEstoqueItemUpdateRequestDto request
    ) {
        MovimentacaoEstoqueItemResponseDto response =
                movimentacaoEstoqueItemService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um item pelo ID.
     *
     * @param id identificador do item
     * @return resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        movimentacaoEstoqueItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}