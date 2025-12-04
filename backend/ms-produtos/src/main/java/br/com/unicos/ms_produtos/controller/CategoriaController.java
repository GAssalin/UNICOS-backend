package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento das categorias de produtos,
 * incluindo criação, atualização, remoção, busca e listagens
 * tanto detalhadas quanto simplificadas.
 * <p>
 * Suporta hierarquia entre categorias (categoria pai e filhos).
 */
@RestController
@RequestMapping("/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // ============================================================
    // 🔹 Criar categoria
    // ============================================================

    /**
     * Cria uma nova categoria, com suporte opcional a categoria pai.
     *
     * @param request DTO contendo nome, descrição e categoriaPaiId.
     * @return Categoria criada.
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> salvar(
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse response = categoriaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar categoria
    // ============================================================

    /**
     * Atualiza uma categoria existente.
     *
     * @param id      ID da categoria a ser atualizada.
     * @param request Dados atualizados.
     * @return Categoria atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse response = categoriaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Buscar por ID
    // ============================================================

    /**
     * Busca uma categoria pelo ID.
     *
     * @param id ID da categoria.
     * @return Categoria encontrada ou 404 caso não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {

        Optional<CategoriaResponse> categoria = categoriaService.buscarPorId(id);

        return categoria
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Listagem detalhada
    // ============================================================

    /**
     * Lista todas as categorias com informações completas.
     *
     * @return Lista detalhada.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // ============================================================
    // 🔹 Listagem simplificada
    // ============================================================

    /**
     * Lista categorias em formato simples (id, nome e informações resumidas).
     *
     * @return Lista simplificada.
     */
    @GetMapping("/simples")
    public ResponseEntity<List<CategoriaListDTO>> listarSimples() {
        return ResponseEntity.ok(categoriaService.listarSimples());
    }

    // ============================================================
    // 🔹 Buscar por nome (contém)
    // ============================================================

    /**
     * Busca categorias pelo nome (ignora maiúsculas/minúsculas).
     *
     * @param nome Nome ou parte do nome.
     * @return Lista de categorias encontradas.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(categoriaService.buscarPorNome(nome));
    }

    // ============================================================
    // 🔹 Deletar categoria
    // ============================================================

    /**
     * Remove uma categoria pelo ID.
     *
     * @param id ID da categoria.
     * @return Status 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 Verificar existência por nome
    // ============================================================

    /**
     * Verifica se existe uma categoria com o nome informado.
     *
     * @param nome Nome a ser verificado.
     * @return true ou false.
     */
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(categoriaService.existePorNome(nome));
    }
}
