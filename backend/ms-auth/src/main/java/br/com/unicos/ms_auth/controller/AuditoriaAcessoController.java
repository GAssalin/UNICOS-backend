package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.service.AuditoriaAcessoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pela consulta dos registros de auditoria de acesso.
 * <p>
 * Fornece endpoints para consultas por usuário, ação e período.
 */
@RestController
@RequestMapping("/v1/auditoria")
public class AuditoriaAcessoController {

    private final AuditoriaAcessoService auditoriaAcessoService;

    /**
     * Injeta a dependência do serviço de auditoria.
     *
     * @param auditoriaAcessoService Serviço de regras de negócio para AuditoriaAcesso.
     */
    public AuditoriaAcessoController(AuditoriaAcessoService auditoriaAcessoService) {
        this.auditoriaAcessoService = auditoriaAcessoService;
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista registros de auditoria por usuário.
     *
     * @param username Nome do usuário.
     * @return Registros de auditoria associados ao username.
     */
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorUsuario(@PathVariable String username) {
        // Útil para investigações ou dashboard de segurança
        List<AuditoriaAcessoResponse> auditorias = auditoriaAcessoService.listarPorUsuario(username);
        return ResponseEntity.ok(auditorias);
    }

    /**
     * Lista registros de auditoria por ação.
     *
     * @param acao Tipo da ação (ex: LOGIN_SUCESSO, LOGIN_FALHA).
     * @return Registros de auditoria do tipo informado.
     */
    @GetMapping("/acao/{acao}")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorAcao(@PathVariable String acao) {
        List<AuditoriaAcessoResponse> auditorias = auditoriaAcessoService.listarPorAcao(acao);
        return ResponseEntity.ok(auditorias);
    }

    /**
     * Lista registros de auditoria ocorridos dentro de um período.
     *
     * @param inicio Data/hora inicial (ISO-8601).
     * @param fim    Data/hora final (ISO-8601).
     * @return Registros de auditoria do intervalo especificado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        // Formato esperado: 2025-11-04T09:30:00
        List<AuditoriaAcessoResponse> auditorias = auditoriaAcessoService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(auditorias);
    }
}
