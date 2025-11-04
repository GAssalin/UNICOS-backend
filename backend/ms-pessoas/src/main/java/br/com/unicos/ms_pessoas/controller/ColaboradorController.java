package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.ColaboradorRequest;
import br.com.unicos.ms_pessoas.dto.ColaboradorResponse;
import br.com.unicos.ms_pessoas.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos colaboradores.
 *
 * <p>Gerencia os vínculos entre pessoas, cargos e empresas, permitindo criar,
 * atualizar, listar e remover colaboradores.</p>
 */
@RestController
@RequestMapping("/v1/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    /**
     * Cria um novo colaborador.
     *
     * @param request DTO com os dados do colaborador
     * @return {@link ColaboradorResponse} criado
     */
    @PostMapping
    public ResponseEntity<ColaboradorResponse> salvar(@Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.salvar(request));
    }

    /**
     * Atualiza os dados de um colaborador.
     *
     * @param id      identificador do colaborador
     * @param request DTO com os novos dados
     * @return {@link ColaboradorResponse} atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.ok(colaboradorService.atualizar(id, request));
    }

    /**
     * Lista todos os colaboradores vinculados a uma empresa.
     *
     * @param empresaId identificador da empresa
     * @return lista de {@link ColaboradorResponse}
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ColaboradorResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(colaboradorService.listarPorEmpresa(empresaId));
    }

    /**
     * Busca um colaborador pelo ID.
     *
     * @param id identificador do colaborador
     * @return {@link ColaboradorResponse} se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> buscarPorId(@PathVariable Long id) {
        return colaboradorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um colaborador.
     *
     * @param id identificador do colaborador
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
