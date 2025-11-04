package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.TokenAcessoResponse;
import br.com.unicos.ms_auth.service.TokenAcessoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos tokens de acesso.
 * <p>
 * Fornece endpoints para criação, listagem e invalidação de tokens JWT.
 */
@RestController
@RequestMapping("/v1/tokens")
public class TokenAcessoController {

    private final TokenAcessoService tokenAcessoService;

    /**
     * Injeta a dependência do serviço de tokens de acesso.
     *
     * @param tokenAcessoService Serviço responsável pela gestão de tokens.
     */
    public TokenAcessoController(TokenAcessoService tokenAcessoService) {
        this.tokenAcessoService = tokenAcessoService;
    }

    // ==================================
    // 🔹 CRUD / GERENCIAMENTO
    // ==================================

    /**
     * Registra (salva) um novo token de acesso para um usuário.
     *
     * @param token     Valor do token JWT.
     * @param usuarioId ID do usuário associado.
     * @return Token salvo com dados de emissão/expiração.
     */
    @PostMapping
    public ResponseEntity<TokenAcessoResponse> registrarToken(@RequestParam String token,
                                                              @RequestParam Long usuarioId) {
        // Cria um registro do token, definindo emissão e expiração padrão
        TokenAcessoResponse response = tokenAcessoService.salvar(token, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca um token pelo valor exato.
     *
     * @param token Valor do token JWT.
     * @return Token encontrado, se existir.
     */
    @GetMapping
    public ResponseEntity<TokenAcessoResponse> buscarPorToken(@RequestParam String token) {
        return tokenAcessoService.buscarPorToken(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os tokens válidos de um usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de tokens ativos para o usuário informado.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TokenAcessoResponse>> listarTokensValidos(@PathVariable Long usuarioId) {
        List<TokenAcessoResponse> tokens = tokenAcessoService.listarTokensValidosPorUsuario(usuarioId);
        return ResponseEntity.ok(tokens);
    }

    /**
     * Invalida (revoga) um token específico.
     *
     * @param token Valor do token a ser invalidado.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @PatchMapping("/invalidar")
    public ResponseEntity<Void> invalidarToken(@RequestParam String token) {
        tokenAcessoService.invalidarToken(token);
        return ResponseEntity.noContent().build();
    }

    /**
     * Invalida todos os tokens válidos de um usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> deletarTokensPorUsuario(@PathVariable Long usuarioId) {
        // Utilizado no fluxo de logout global / force logout
        tokenAcessoService.deletarPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
