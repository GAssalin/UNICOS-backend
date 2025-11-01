package br.com.erp.ms_ativos.controller;

import br.com.erp.ms_ativos.dto.AtivoRequest;
import br.com.erp.ms_ativos.dto.AtivoResponse;
import br.com.erp.ms_ativos.enums.StatusAtivo;
import br.com.erp.ms_ativos.enums.TipoAtivo;
import br.com.erp.ms_ativos.service.AtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de ativos.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de ativos.
 */
@RestController
@RequestMapping("/v1/ativos")
@RequiredArgsConstructor
public class AtivoController {

    private final AtivoService ativoService;

    /**
     * Cria um novo ativo.
     *
     * @param request dados do ativo
     * @return ativo criado
     */
    @PostMapping
    public ResponseEntity<AtivoResponse> criar(@Valid @RequestBody AtivoRequest request) {
        AtivoResponse response = ativoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um ativo existente.
     *
     * @param id identificador do ativo
     * @param request novos dados do ativo
     * @return ativo atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtivoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtivoRequest request) {
        AtivoResponse response = ativoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um ativo pelo seu ID.
     *
     * @param id identificador do ativo
     * @return ativo encontrado, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtivoResponse> buscarPorId(@PathVariable Long id) {
        return ativoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca um ativo pelo código patrimonial.
     *
     * @param codigo código patrimonial do ativo
     * @return ativo encontrado, se existir
     */
    @GetMapping("/buscar/codigo-patrimonial")
    public ResponseEntity<AtivoResponse> buscarPorCodigo(@RequestParam String codigo) {
        return ativoService.buscarPorCodigoPatrimonial(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os ativos cadastrados.
     *
     * @return lista de ativos
     */
    @GetMapping
    public ResponseEntity<List<AtivoResponse>> listarTodos() {
        List<AtivoResponse> ativos = ativoService.listarTodos();
        if (ativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ativos);
    }

    /**
     * Lista ativos por empresa.
     *
     * @param empresaId identificador da empresa
     * @return lista de ativos vinculados à empresa
     */
    @GetMapping("/buscar/empresa")
    public ResponseEntity<List<AtivoResponse>> buscarPorEmpresa(@RequestParam Long empresaId) {
        List<AtivoResponse> ativos = ativoService.buscarPorEmpresa(empresaId);
        if (ativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ativos);
    }

    /**
     * Lista ativos por filial.
     *
     * @param filialId identificador da filial
     * @return lista de ativos vinculados à filial
     */
    @GetMapping("/buscar/filial")
    public ResponseEntity<List<AtivoResponse>> buscarPorFilial(@RequestParam Long filialId) {
        List<AtivoResponse> ativos = ativoService.buscarPorFilial(filialId);
        if (ativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ativos);
    }

    /**
     * Lista ativos por tipo.
     *
     * @param tipo tipo de ativo
     * @return lista de ativos correspondentes
     */
    @GetMapping("/buscar/tipo")
    public ResponseEntity<List<AtivoResponse>> buscarPorTipo(@RequestParam TipoAtivo tipo) {
        List<AtivoResponse> ativos = ativoService.buscarPorTipo(tipo);
        if (ativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ativos);
    }

    /**
     * Lista ativos por status.
     *
     * @param status status do ativo
     * @return lista de ativos correspondentes
     */
    @GetMapping("/buscar/status")
    public ResponseEntity<List<AtivoResponse>> buscarPorStatus(@RequestParam StatusAtivo status) {
        List<AtivoResponse> ativos = ativoService.buscarPorStatus(status);
        if (ativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ativos);
    }

    /**
     * Exclui um ativo pelo seu ID.
     *
     * @param id identificador do ativo
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ativoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}