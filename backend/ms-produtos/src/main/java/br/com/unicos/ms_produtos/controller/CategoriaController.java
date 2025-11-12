package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.CategoriaResponse;
import br.com.unicos.ms_produtos.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das categorias.
 *
 * Fornece endpoints para operações de CRUD e consultas específicas.
 */
@RestController
@RequestMapping("/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria uma nova categoria.
     *
     * @param request Dados da categoria a ser criada.
     * @return CategoriaResponseDTO representando a categoria criada.
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> criarCategoria(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse response = categoriaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id      Identificador da categoria.
     * @param request Dados atualizados da categoria.
     * @return CategoriaResponseDTO atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long id,
                                                                @Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse response = categoriaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma categoria pelo ID.
     *
     * @param id Identificador da categoria.
     * @return CategoriaResponseDTO encontrada, se existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as categorias.
     *
     * @return Lista completa de categorias.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        List<CategoriaResponse> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Lista categorias simplificadas (id + nome), ideal para combos e seletores.
     *
     * @return Lista simples de categorias.
     */
    @GetMapping("/simples")
    public ResponseEntity<List<CategoriaListDTO>> listarSimples() {
        List<CategoriaListDTO> categorias = categoriaService.listarSimples();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Busca categorias pelo nome (parcial ou completo).
     *
     * @param nome Nome ou parte do nome da categoria.
     * @return Lista de categorias que correspondem ao termo.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNome(@RequestParam String nome) {
        List<CategoriaResponse> categorias = categoriaService.buscarPorNome(nome);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Exclui uma categoria pelo ID.
     *
     * @param id Identificador da categoria.
     * @return Resposta 204 (sem conteúdo) se a exclusão for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica se já existe uma categoria com o nome informado.
     *
     * @param nome Nome da categoria.
     * @return true se o nome já estiver cadastrado, false caso contrário.
     */
    @GetMapping("/verificar-nome")
    public ResponseEntity<Boolean> verificarNome(@RequestParam String nome) {
        boolean existe = categoriaService.existePorNome(nome);
        return ResponseEntity.ok(existe);
    }
}