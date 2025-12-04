package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.service.AtributoPersonalizadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos
 * atributos personalizados associados às categorias de produtos.
 * <p>
 * Expõe endpoints para criação, atualização, exclusão,
 * listagem e consulta de atributos vinculados às categorias.
 */
@RestController
@RequestMapping("/v1/atributos-personalizados")
@RequiredArgsConstructor
public class AtributoPersonalizadoController {

    private final AtributoPersonalizadoService atributoPersonalizadoService;

    // ============================================================
    // 🔹 Criar
    // ============================================================

    /**
     * Cria um novo atributo personalizado vinculado a uma categoria.
     *
     * @param request DTO contendo os dados do atributo.
     * @return Atributo criado.
     */
    @PostMapping
    public ResponseEntity<AtributoPersonalizadoResponse> criar(
            @Valid @RequestBody AtributoPersonalizadoRequest request) {

        AtributoPersonalizadoResponse response = atributoPersonalizadoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar
    // ============================================================

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      ID do atributo.
     * @param request DTO contendo os novos dados.
     * @return Atributo atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtributoPersonalizadoRequest request) {

        AtributoPersonalizadoResponse response = atributoPersonalizadoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Excluir
    // ============================================================

    /**
     * Remove um atributo personalizado pelo ID.
     *
     * @param id ID do atributo.
     * @return Status 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        atributoPersonalizadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 Buscar por ID
    // ============================================================

    /**
     * Busca um atributo personalizado pelo ID.
     *
     * @param id ID do atributo.
     * @return Atributo encontrado ou 404 caso não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> buscarPorId(@PathVariable Long id) {

        Optional<AtributoPersonalizadoResponse> resultado =
                atributoPersonalizadoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Listar todos
    // ============================================================

    /**
     * Lista todos os atributos personalizados existentes.
     *
     * @return Lista de atributos.
     */
    @GetMapping
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> listarTodos() {
        return ResponseEntity.ok(atributoPersonalizadoService.listarTodos());
    }

    // ============================================================
    // 🔹 Listar por categoria
    // ============================================================

    /**
     * Lista os atributos personalizados pertencentes a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de atributos vinculados à categoria.
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(atributoPersonalizadoService.listarPorCategoria(categoriaId));
    }
}
