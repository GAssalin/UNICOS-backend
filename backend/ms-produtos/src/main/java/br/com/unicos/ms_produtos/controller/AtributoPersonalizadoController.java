package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.service.AtributoPersonalizadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos atributos personalizados
 * vinculados às categorias de produtos.
 *
 * <p>
 * Permite a criação, atualização, listagem, exclusão e consulta
 * de atributos configuráveis de categorias (ex: "Cor", "Tamanho").
 * </p>
 */
@RestController
@RequestMapping("/v1/atributos-personalizados")
public class AtributoPersonalizadoController {

    private final AtributoPersonalizadoService atributoPersonalizadoService;

    public AtributoPersonalizadoController(AtributoPersonalizadoService atributoPersonalizadoService) {
        this.atributoPersonalizadoService = atributoPersonalizadoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo atributo personalizado para uma categoria.
     *
     * @param request Dados do atributo personalizado.
     * @return {@link AtributoPersonalizadoResponse} criado.
     */
    @PostMapping
    public ResponseEntity<AtributoPersonalizadoResponse> criar(
            @Valid @RequestBody AtributoPersonalizadoRequest request) {
        AtributoPersonalizadoResponse response = atributoPersonalizadoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      Identificador do atributo.
     * @param request Dados atualizados do atributo.
     * @return {@link AtributoPersonalizadoResponse} atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtributoPersonalizadoRequest request) {
        AtributoPersonalizadoResponse response = atributoPersonalizadoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um atributo personalizado pelo seu ID.
     *
     * @param id Identificador do atributo.
     * @return {@link AtributoPersonalizadoResponse}, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> buscarPorId(@PathVariable Long id) {
        return atributoPersonalizadoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os atributos personalizados cadastrados no sistema.
     *
     * @return Lista de {@link AtributoPersonalizadoResponse}.
     */
    @GetMapping
    public ResponseEntity<List<AtributoPersonalizadoResponse>> listarTodos() {
        List<AtributoPersonalizadoResponse> lista = atributoPersonalizadoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Exclui um atributo personalizado pelo ID.
     *
     * @param id Identificador do atributo.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atributoPersonalizadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todos os atributos personalizados de uma categoria.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de {@link AtributoPersonalizadoResponse}.
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<AtributoPersonalizadoResponse>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<AtributoPersonalizadoResponse> lista = atributoPersonalizadoService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca atributos personalizados cujo nome contenha o termo informado.
     *
     * @param nome Termo parcial de busca.
     * @return Lista de {@link AtributoPersonalizadoListDTO}.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> buscarPorNomeContendo(@RequestParam String nome) {
        List<AtributoPersonalizadoListDTO> lista = atributoPersonalizadoService.buscarPorNomeContendo(nome);
        return ResponseEntity.ok(lista);
    }
}
