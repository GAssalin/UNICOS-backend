package br.com.erp.ms_empresa.controller;

import br.com.erp.ms_empresa.dto.ConfiguracaoEmpresaRequest;
import br.com.erp.ms_empresa.dto.ConfiguracaoEmpresaResponse;
import br.com.erp.ms_empresa.service.ConfiguracaoFiscalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento das configurações fiscais das empresas.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de configurações fiscais.
 */
@RestController
@RequestMapping("/v1/configuracoes-fiscais")
@RequiredArgsConstructor
public class ConfiguracaoFiscalController {

    private final ConfiguracaoFiscalService configuracaoFiscalService;

    /**
     * Cria uma nova configuração fiscal para uma empresa.
     *
     * @param request dados da configuração fiscal
     * @return configuração criada
     */
    @PostMapping
    public ResponseEntity<ConfiguracaoEmpresaResponse> criar(@Valid @RequestBody ConfiguracaoEmpresaRequest request) {
        ConfiguracaoEmpresaResponse response = configuracaoFiscalService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma configuração fiscal existente.
     *
     * @param id identificador da configuração fiscal
     * @param request dados atualizados da configuração fiscal
     * @return configuração atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConfiguracaoEmpresaRequest request) {

        ConfiguracaoEmpresaResponse response = configuracaoFiscalService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma configuração fiscal pelo seu ID.
     *
     * @param id identificador da configuração fiscal
     * @return configuração encontrada, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> buscarPorId(@PathVariable Long id) {
        return configuracaoFiscalService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma configuração fiscal vinculada a uma empresa específica.
     *
     * @param empresaId identificador da empresa
     * @return configuração fiscal da empresa, se existir
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> buscarPorEmpresa(@PathVariable Long empresaId) {
        return configuracaoFiscalService.buscarPorEmpresa(empresaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as configurações fiscais cadastradas.
     *
     * @return lista de configurações fiscais
     */
    @GetMapping
    public ResponseEntity<List<ConfiguracaoEmpresaResponse>> listarTodas() {
        List<ConfiguracaoEmpresaResponse> configuracoes = configuracaoFiscalService.listarTodas();
        if (configuracoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(configuracoes);
    }

    /**
     * Exclui uma configuração fiscal pelo ID informado.
     *
     * @param id identificador da configuração fiscal
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        configuracaoFiscalService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}