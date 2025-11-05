package br.com.unicos.ms_ativos.controller;

import br.com.unicos.ms_ativos.dto.DocumentoAtivoListDTO;
import br.com.unicos.ms_ativos.dto.DocumentoAtivoRequest;
import br.com.unicos.ms_ativos.dto.DocumentoAtivoResponse;
import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;
import br.com.unicos.ms_ativos.service.DocumentoAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de documentos vinculados aos ativos.
 * <p>
 * Permite o cadastro, atualização, exclusão e consultas filtradas por tipo, status,
 * período e validade de documentos como notas fiscais, laudos, certificados e garantias.
 */
@RestController
@RequestMapping("/v1/documentos-ativos")
@RequiredArgsConstructor
public class DocumentoAtivoController {

    private final DocumentoAtivoService documentoAtivoService;

    // ===========================================================
    // 🔹 CRUD BÁSICO
    // ===========================================================

    /**
     * Registra um novo documento vinculado a um ativo.
     *
     * @param request DTO contendo os dados do documento.
     * @return resposta com os dados do documento criado.
     */
    @PostMapping
    public ResponseEntity<DocumentoAtivoResponse> salvar(@Valid @RequestBody DocumentoAtivoRequest request) {
        DocumentoAtivoResponse response = documentoAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um documento existente.
     *
     * @param id      identificador do documento.
     * @param request DTO contendo os novos dados.
     * @return resposta com os dados atualizados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoAtivoResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody DocumentoAtivoRequest request) {
        DocumentoAtivoResponse response = documentoAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um documento com base no seu ID.
     *
     * @param id identificador do documento.
     * @return status HTTP 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        documentoAtivoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca um documento específico pelo seu ID.
     *
     * @param id identificador do documento.
     * @return resposta com os dados detalhados, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoAtivoResponse> buscarPorId(@PathVariable Long id) {
        return documentoAtivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os documentos cadastrados.
     *
     * @return lista de documentos.
     */
    @GetMapping
    public ResponseEntity<List<DocumentoAtivoListDTO>> listarTodos() {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ===========================================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ===========================================================

    /**
     * Retorna todos os documentos vinculados a um ativo específico.
     *
     * @param ativoId identificador do ativo.
     * @return lista de documentos associados ao ativo.
     */
    @GetMapping("/ativo/{ativoId}")
    public ResponseEntity<List<DocumentoAtivoListDTO>> buscarPorAtivo(@PathVariable Long ativoId) {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.buscarPorAtivo(ativoId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os documentos de um determinado tipo (nota fiscal, garantia, etc.).
     *
     * @param tipo tipo do documento.
     * @return lista de documentos do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DocumentoAtivoListDTO>> buscarPorTipo(@PathVariable TipoDocumentoAtivo tipo) {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os documentos com um status específico.
     *
     * @param status status do documento.
     * @return lista de documentos com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DocumentoAtivoListDTO>> buscarPorStatus(@PathVariable StatusDocumento status) {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.buscarPorStatus(status);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna documentos emitidos dentro de um intervalo de datas.
     *
     * @param inicio data inicial.
     * @param fim    data final.
     * @return lista de documentos emitidos no período.
     */
    @GetMapping("/periodo-emissao")
    public ResponseEntity<List<DocumentoAtivoListDTO>> buscarPorPeriodoEmissao(@RequestParam LocalDate inicio,
                                                                               @RequestParam LocalDate fim) {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.buscarPorPeriodoEmissao(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna a última nota fiscal emitida para um ativo.
     *
     * @param ativoId identificador do ativo.
     * @return última nota fiscal registrada, se existente.
     */
    @GetMapping("/ativo/{ativoId}/ultima-nota-fiscal")
    public ResponseEntity<DocumentoAtivoResponse> buscarUltimaNotaFiscal(@PathVariable Long ativoId) {
        Optional<DocumentoAtivoResponse> response = documentoAtivoService.buscarUltimaNotaFiscal(ativoId);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna todas as garantias ainda válidas no sistema.
     *
     * @return lista de garantias válidas.
     */
    @GetMapping("/garantias-validas")
    public ResponseEntity<List<DocumentoAtivoListDTO>> buscarGarantiasValidas() {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.buscarGarantiasValidas();
        return ResponseEntity.ok(lista);
    }

    /**
     * Retorna todos os documentos vencidos ou expirados.
     *
     * @return lista de documentos vencidos.
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<DocumentoAtivoListDTO>> buscarDocumentosVencidos() {
        List<DocumentoAtivoListDTO> lista = documentoAtivoService.buscarDocumentosVencidos();
        return ResponseEntity.ok(lista);
    }
}
