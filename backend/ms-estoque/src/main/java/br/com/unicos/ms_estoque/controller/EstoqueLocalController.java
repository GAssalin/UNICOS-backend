package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.EstoqueLocalListDTO;
import br.com.unicos.ms_estoque.dto.EstoqueLocalRequest;
import br.com.unicos.ms_estoque.dto.EstoqueLocalResponse;
import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;
import br.com.unicos.ms_estoque.service.EstoqueLocalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos locais de estoque.
 * <p>
 * Permite operações de criação, atualização, listagem, exclusão e busca
 * por tipo ou empresa associada.
 */
@RestController
@RequestMapping("/v1/estoques-locais")
@RequiredArgsConstructor
public class EstoqueLocalController {

    private final EstoqueLocalService estoqueLocalService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo local de estoque.
     *
     * @param request DTO com os dados do local de estoque.
     * @return Dados do local criado.
     */
    @PostMapping
    public ResponseEntity<EstoqueLocalResponse> criar(@Valid @RequestBody EstoqueLocalRequest request) {
        EstoqueLocalResponse response = estoqueLocalService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um local de estoque existente.
     *
     * @param id      ID do local.
     * @param request DTO com novos dados.
     * @return Local atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstoqueLocalResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstoqueLocalRequest request
    ) {
        EstoqueLocalResponse response = estoqueLocalService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um local de estoque pelo ID.
     *
     * @param id ID do local.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        estoqueLocalService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todos os locais de estoque cadastrados.
     *
     * @return Lista de locais resumidos.
     */
    @GetMapping
    public ResponseEntity<List<EstoqueLocalListDTO>> listarTodos() {
        return ResponseEntity.ok(estoqueLocalService.listarTodos());
    }

    /**
     * Busca um local de estoque pelo ID.
     *
     * @param id ID do local.
     * @return Dados completos do local.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueLocalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueLocalService.buscarPorId(id));
    }

    /**
     * Busca locais de estoque por tipo.
     *
     * @param tipo Tipo do local (ex: DEPOSITO, LOJA, TERCEIRO).
     * @return Lista de locais do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<EstoqueLocalListDTO>> buscarPorTipo(@PathVariable TipoLocalEstoque tipo) {
        return ResponseEntity.ok(estoqueLocalService.buscarPorTipo(tipo));
    }

    /**
     * Busca locais de estoque vinculados a uma empresa.
     *
     * @param empresaId ID da empresa.
     * @return Lista de locais da empresa.
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EstoqueLocalListDTO>> buscarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(estoqueLocalService.buscarPorEmpresa(empresaId));
    }
}
