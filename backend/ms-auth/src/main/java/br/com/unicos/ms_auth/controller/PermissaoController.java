package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.PermissaoRequest;
import br.com.unicos.ms_auth.dto.PermissaoResponse;
import br.com.unicos.ms_auth.service.PermissaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das permissões.
 * <p>
 * Fornece endpoints para operações de CRUD e consultas específicas.
 */
@RestController
@RequestMapping("/v1/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;

    /**
     * Injeta a dependência do serviço de permissões.
     *
     * @param permissaoService Serviço responsável pelas regras de negócio de Permissão.
     */
    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria uma nova permissão.
     *
     * @param request Dados da permissão a ser criada.
     * @return Permissão criada.
     */
    @PostMapping
    public ResponseEntity<PermissaoResponse> criarPermissao(@Valid @RequestBody PermissaoRequest request) {
        // Persiste a permissão com nome único
        PermissaoResponse response = permissaoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma permissão existente.
     *
     * @param id      Identificador da permissão.
     * @param request Dados atualizados da permissão.
     * @return Permissão atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponse> atualizarPermissao(@PathVariable Long id,
                                                                @Valid @RequestBody PermissaoRequest request) {
        PermissaoResponse response = permissaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma permissão pelo ID.
     *
     * @param id Identificador da permissão.
     * @return Permissão encontrada, se existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoResponse> buscarPorId(@PathVariable Long id) {
        return permissaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as permissões.
     *
     * @return Lista completa de permissões.
     */
    @GetMapping
    public ResponseEntity<List<PermissaoResponse>> listarTodas() {
        List<PermissaoResponse> permissoes = permissaoService.listarTodas();
        return ResponseEntity.ok(permissoes);
    }

    /**
     * Exclui uma permissão pelo ID.
     *
     * @param id Identificador da permissão.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPermissao(@PathVariable Long id) {
        permissaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Verifica se já existe uma permissão com o nome informado.
     *
     * @param nome Nome da permissão.
     * @return true se já existir, false caso contrário.
     */
    @GetMapping("/verificar-nome")
    public ResponseEntity<Boolean> verificarNome(@RequestParam String nome) {
        boolean existe = permissaoService.existePorNome(nome);
        return ResponseEntity.ok(existe);
    }
}
