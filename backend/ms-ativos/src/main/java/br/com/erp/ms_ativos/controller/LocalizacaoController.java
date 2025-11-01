package br.com.erp.ms_ativos.controller;

import br.com.erp.ms_ativos.dto.LocalizacaoRequest;
import br.com.erp.ms_ativos.dto.LocalizacaoResponse;
import br.com.erp.ms_ativos.service.LocalizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de localizações.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de localizações.
 */
@RestController
@RequestMapping("/v1/localizacoes")
@RequiredArgsConstructor
public class LocalizacaoController {

    private final LocalizacaoService localizacaoService;

    /**
     * Cria uma nova localização.
     *
     * @param request dados da localização
     * @return localização criada
     */
    @PostMapping
    public ResponseEntity<LocalizacaoResponse> criar(@Valid @RequestBody LocalizacaoRequest request) {
        LocalizacaoResponse response = localizacaoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de uma localização existente.
     *
     * @param id identificador da localização
     * @param request novos dados
     * @return localização atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocalizacaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LocalizacaoRequest request) {
        LocalizacaoResponse response = localizacaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma localização pelo ID.
     *
     * @param id identificador da localização
     * @return localização encontrada, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocalizacaoResponse> buscarPorId(@PathVariable Long id) {
        return localizacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as localizações cadastradas.
     *
     * @return lista de localizações
     */
    @GetMapping
    public ResponseEntity<List<LocalizacaoResponse>> listarTodas() {
        List<LocalizacaoResponse> lista = localizacaoService.listarTodas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista localizações por filial.
     *
     * @param filialId identificador da filial
     * @return lista de localizações da filial
     */
    @GetMapping("/buscar/filial")
    public ResponseEntity<List<LocalizacaoResponse>> buscarPorFilial(@RequestParam Long filialId) {
        List<LocalizacaoResponse> lista = localizacaoService.buscarPorFilial(filialId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    /**
     * Exclui uma localização.
     *
     * @param id identificador da localização
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        localizacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}