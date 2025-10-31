package br.com.erp.ms_empresa.controller;

import br.com.erp.ms_empresa.dto.ContatoEmpresaRequest;
import br.com.erp.ms_empresa.dto.ContatoEmpresaResponse;
import br.com.erp.ms_empresa.service.ContatoEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos contatos corporativos das empresas.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de contatos.
 */
@RestController
@RequestMapping("/v1/contatos-empresa")
@RequiredArgsConstructor
public class ContatoEmpresaController {

    private final ContatoEmpresaService contatoEmpresaService;

    /**
     * Cria um novo contato empresarial.
     *
     * @param request dados do contato a ser criado
     * @return contato criado
     */
    @PostMapping
    public ResponseEntity<ContatoEmpresaResponse> criar(@Valid @RequestBody ContatoEmpresaRequest request) {
        ContatoEmpresaResponse response = contatoEmpresaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um contato existente.
     *
     * @param id identificador do contato
     * @param request novos dados do contato
     * @return contato atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContatoEmpresaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContatoEmpresaRequest request) {
        ContatoEmpresaResponse response = contatoEmpresaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um contato pelo seu ID.
     *
     * @param id identificador do contato
     * @return contato encontrado, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContatoEmpresaResponse> buscarPorId(@PathVariable Long id) {
        return contatoEmpresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os contatos cadastrados.
     *
     * @return lista de contatos
     */
    @GetMapping
    public ResponseEntity<List<ContatoEmpresaResponse>> listarTodos() {
        List<ContatoEmpresaResponse> contatos = contatoEmpresaService.listarTodos();
        if (contatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contatos);
    }

    /**
     * Lista todos os contatos vinculados a uma empresa específica.
     *
     * @param empresaId identificador da empresa
     * @return lista de contatos da empresa
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ContatoEmpresaResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<ContatoEmpresaResponse> contatos = contatoEmpresaService.listarPorEmpresa(empresaId);
        if (contatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contatos);
    }

    /**
     * Busca contatos cujo nome contenha determinado termo.
     *
     * @param nome termo de busca
     * @return lista de contatos correspondentes
     */
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<ContatoEmpresaResponse>> buscarPorNome(@RequestParam String nome) {
        List<ContatoEmpresaResponse> contatos = contatoEmpresaService.buscarPorNome(nome);
        if (contatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contatos);
    }

    /**
     * Busca um contato pelo e-mail informado.
     *
     * @param email e-mail do contato
     * @return contato encontrado, se existir
     */
    @GetMapping("/buscar/email")
    public ResponseEntity<ContatoEmpresaResponse> buscarPorEmail(@RequestParam String email) {
        return contatoEmpresaService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um contato pelo seu ID.
     *
     * @param id identificador do contato
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoEmpresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}