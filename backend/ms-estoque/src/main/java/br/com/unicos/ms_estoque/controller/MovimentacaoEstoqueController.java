package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueSearchRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;
import br.com.unicos.ms_estoque.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller responsável pelos endpoints de gerenciamento de movimentações de estoque.
 */
@RestController
@RequestMapping("/v1/movimentacoes-estoque")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    /**
     * Cadastra uma nova movimentação de estoque.
     *
     * @param request dados para criação da movimentação
     * @return movimentação criada
     */
    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponseDto> criar(
            @Valid @RequestBody MovimentacaoEstoqueCreateRequestDto request
    ) {
        MovimentacaoEstoqueResponseDto response = movimentacaoEstoqueService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca uma movimentação pelo ID.
     *
     * @param id identificador da movimentação
     * @return movimentação encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> buscarPorId(@PathVariable Long id) {
        MovimentacaoEstoqueResponseDto response = movimentacaoEstoqueService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as movimentações de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página de movimentações
     */
    @GetMapping
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> listar(Pageable pageable) {
        Page<MovimentacaoEstoqueResponseDto> response = movimentacaoEstoqueService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista movimentações por tipo.
     *
     * @param tipoMovimentacao tipo da movimentação
     * @param pageable parâmetros de paginação
     * @return página de movimentações
     */
    @GetMapping("/tipo/{tipoMovimentacao}")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> listarPorTipo(
            @PathVariable TipoMovimentacaoEstoque tipoMovimentacao,
            Pageable pageable
    ) {
        Page<MovimentacaoEstoqueResponseDto> response =
                movimentacaoEstoqueService.listarPorTipo(tipoMovimentacao, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista movimentações por status.
     *
     * @param statusMovimentacao status da movimentação
     * @param pageable parâmetros de paginação
     * @return página de movimentações
     */
    @GetMapping("/status/{statusMovimentacao}")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> listarPorStatus(
            @PathVariable StatusMovimentacaoEstoque statusMovimentacao,
            Pageable pageable
    ) {
        Page<MovimentacaoEstoqueResponseDto> response =
                movimentacaoEstoqueService.listarPorStatus(statusMovimentacao, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista movimentações por estoque de origem.
     *
     * @param estoqueOrigemId identificador do estoque de origem
     * @param pageable parâmetros de paginação
     * @return página de movimentações
     */
    @GetMapping("/estoque-origem/{estoqueOrigemId}")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> listarPorEstoqueOrigem(
            @PathVariable Long estoqueOrigemId,
            Pageable pageable
    ) {
        Page<MovimentacaoEstoqueResponseDto> response =
                movimentacaoEstoqueService.listarPorEstoqueOrigem(estoqueOrigemId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista movimentações por estoque de destino.
     *
     * @param estoqueDestinoId identificador do estoque de destino
     * @param pageable parâmetros de paginação
     * @return página de movimentações
     */
    @GetMapping("/estoque-destino/{estoqueDestinoId}")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> listarPorEstoqueDestino(
            @PathVariable Long estoqueDestinoId,
            Pageable pageable
    ) {
        Page<MovimentacaoEstoqueResponseDto> response =
                movimentacaoEstoqueService.listarPorEstoqueDestino(estoqueDestinoId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma movimentação pelo documento de referência.
     *
     * @param documentoReferencia documento de referência
     * @return movimentação encontrada
     */
    @GetMapping("/documento/{documentoReferencia}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> buscarPorDocumentoReferencia(
            @PathVariable String documentoReferencia
    ) {
        MovimentacaoEstoqueResponseDto response =
                movimentacaoEstoqueService.buscarPorDocumentoReferencia(documentoReferencia);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista movimentações dentro de um período.
     *
     * @param dataInicial data inicial
     * @param dataFinal data final
     * @param pageable parâmetros de paginação
     * @return página de movimentações
     */
    @GetMapping("/periodo")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> listarPorPeriodo(
            @RequestParam LocalDateTime dataInicial,
            @RequestParam LocalDateTime dataFinal,
            Pageable pageable
    ) {
        Page<MovimentacaoEstoqueResponseDto> response =
                movimentacaoEstoqueService.listarPorPeriodo(dataInicial, dataFinal, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Pesquisa movimentações com base nos filtros informados.
     *
     * @param tipoMovimentacao tipo da movimentação
     * @param estoqueOrigemId estoque de origem
     * @param estoqueDestinoId estoque de destino
     * @param documentoReferencia documento de referência
     * @param usuarioResponsavelId usuário responsável
     * @param statusMovimentacao status da movimentação
     * @param dataInicial data inicial
     * @param dataFinal data final
     * @param pageable parâmetros de paginação
     * @return página de movimentações filtradas
     */
    @GetMapping("/pesquisa")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDto>> pesquisar(
            @RequestParam(required = false) TipoMovimentacaoEstoque tipoMovimentacao,
            @RequestParam(required = false) Long estoqueOrigemId,
            @RequestParam(required = false) Long estoqueDestinoId,
            @RequestParam(required = false) String documentoReferencia,
            @RequestParam(required = false) Long usuarioResponsavelId,
            @RequestParam(required = false) StatusMovimentacaoEstoque statusMovimentacao,
            @RequestParam(required = false) LocalDateTime dataInicial,
            @RequestParam(required = false) LocalDateTime dataFinal,
            Pageable pageable
    ) {
        MovimentacaoEstoqueSearchRequestDto request = new MovimentacaoEstoqueSearchRequestDto(
                tipoMovimentacao,
                estoqueOrigemId,
                estoqueDestinoId,
                documentoReferencia,
                usuarioResponsavelId,
                statusMovimentacao,
                dataInicial,
                dataFinal
        );

        Page<MovimentacaoEstoqueResponseDto> response =
                movimentacaoEstoqueService.pesquisar(request, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza os dados de uma movimentação.
     *
     * @param id identificador da movimentação
     * @param request dados para atualização
     * @return movimentação atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoEstoqueUpdateRequestDto request
    ) {
        MovimentacaoEstoqueResponseDto response = movimentacaoEstoqueService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove uma movimentação pelo ID.
     *
     * @param id identificador da movimentação
     * @return resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        movimentacaoEstoqueService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}