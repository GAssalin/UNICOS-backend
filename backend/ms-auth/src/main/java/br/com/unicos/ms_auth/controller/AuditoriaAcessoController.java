package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.service.interfaces.AuditoriaAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pela consulta dos registros
 * de auditoria de acesso do sistema.
 * <p>
 * Permite filtros por usuário, ação e intervalo de datas,
 * fornecendo uma interface completa para auditorias e segurança.
 */
@RestController
@RequestMapping("/v1/auditorias")
@RequiredArgsConstructor
public class AuditoriaAcessoController {

    private final AuditoriaAcessoService auditoriaAcessoService;

    // ================================================
    // 🔍 CONSULTA POR USUÁRIO
    // ================================================

    /**
     * Lista todos os eventos de auditoria associados a um usuário específico.
     *
     * @param username Nome do usuário.
     * @return Lista de AuditoriaAcessoResponse.
     */
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorUsuario(
            @PathVariable String username) {

        return ResponseEntity.ok(auditoriaAcessoService.listarPorUsuario(username));
    }

    // ================================================
    // 🔍 CONSULTA POR TIPO DE AÇÃO
    // ================================================

    /**
     * Lista todos os eventos de auditoria para um tipo de ação específico.
     *
     * @param acao Tipo da ação (ex: LOGIN_SUCESSO, LOGIN_FALHA, LOGOUT).
     * @return Lista de AuditoriaAcessoResponse.
     */
    @GetMapping("/acao/{acao}")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorAcao(
            @PathVariable TipoAcaoAcesso acao) {

        return ResponseEntity.ok(auditoriaAcessoService.listarPorAcao(acao));
    }

    // ================================================
    // 🔍 CONSULTA POR PERÍODO
    // ================================================

    /**
     * Lista eventos registrados dentro de um intervalo de datas.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de AuditoriaAcessoResponse.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        return ResponseEntity.ok(auditoriaAcessoService.listarPorPeriodo(inicio, fim));
    }
}
