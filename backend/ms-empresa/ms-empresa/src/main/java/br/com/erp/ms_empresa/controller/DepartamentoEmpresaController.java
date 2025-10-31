package br.com.erp.ms_empresa.controller;

import br.com.erp.ms_empresa.dto.DepartamentoEmpresaRequest;
import br.com.erp.ms_empresa.dto.DepartamentoEmpresaResponse;
import br.com.erp.ms_empresa.service.DepartamentoEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos departamentos das empresas.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de departamentos.
 */
@RestController
@RequestMapping("/v1/departamentos-empresa")
@RequiredArgsConstructor
public class DepartamentoEmpresaController {

    private final DepartamentoEmpresaService departamentoEmpresaService;

    /**
     * Cria um novo departamento empresarial.
     *
     * @param request dados do departamento a ser criado
     * @return departamento criado
     */
    @PostMapping
    public ResponseEntity<DepartamentoEmpresaResponse> criar(@Valid @RequestBody DepartamentoEmpresaRequest request) {
        DepartamentoEmpresaResponse response = departamentoEmpresaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um departamento existente.
     *
     * @param id identificador do departamento
     * @param request novos dados
     * @return departamento atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoEmpresaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoEmpresaRequest request) {
        DepartamentoEmpresaResponse response = departamentoEmpresaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um departamento pelo seu ID.
     *
     * @param id identificador do departamento
     * @return departamento encontrado, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoEmpresaResponse> buscarPorId(@PathVariable Long id) {
        return departamentoEmpresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os departamentos cadastrados.
     *
     * @return lista de departamentos
     */
    @GetMapping
    public ResponseEntity<List<DepartamentoEmpresaResponse>> listarTodos() {
        List<DepartamentoEmpresaResponse> departamentos = departamentoEmpresaService.listarTodos();
        if (departamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departamentos);
    }

    /**
     * Lista todos os departamentos vinculados a uma empresa específica.
     *
     * @param empresaId identificador da empresa
     * @return lista de departamentos da empresa
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<DepartamentoEmpresaResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<DepartamentoEmpresaResponse> departamentos = departamentoEmpresaService.listarPorEmpresa(empresaId);
        if (departamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departamentos);
    }

    /**
     * Busca departamentos cujo nome contenha determinado termo.
     *
     * @param nome termo de busca
     * @return lista de departamentos correspondentes
     */
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<DepartamentoEmpresaResponse>> buscarPorNome(@RequestParam String nome) {
        List<DepartamentoEmpresaResponse> departamentos = departamentoEmpresaService.buscarPorNome(nome);
        if (departamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departamentos);
    }

    /**
     * Lista apenas os departamentos ativos.
     *
     * @return lista de departamentos ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<DepartamentoEmpresaResponse>> listarAtivos() {
        List<DepartamentoEmpresaResponse> ativos = departamentoEmpresaService.listarAtivos();
        if (ativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ativos);
    }

    /**
     * Lista apenas os departamentos inativos.
     *
     * @return lista de departamentos inativos
     */
    @GetMapping("/inativos")
    public ResponseEntity<List<DepartamentoEmpresaResponse>> listarInativos() {
        List<DepartamentoEmpresaResponse> inativos = departamentoEmpresaService.listarInativos();
        if (inativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inativos);
    }

    /**
     * Exclui um departamento pelo seu ID.
     *
     * @param id identificador do departamento
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoEmpresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}