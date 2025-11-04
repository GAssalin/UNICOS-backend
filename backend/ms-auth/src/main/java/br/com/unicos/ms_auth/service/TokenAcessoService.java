package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.TokenAcessoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade TokenAcesso.
 */
public interface TokenAcessoService {

    /**
     * Registra um novo token de acesso JWT.
     *
     * @param token     Valor do token JWT.
     * @param usuarioId ID do usuário associado.
     * @return TokenAcessoResponse criado.
     */
    TokenAcessoResponse salvar(String token, Long usuarioId);

    /**
     * Invalida um token específico.
     *
     * @param token Valor do token JWT.
     */
    void invalidarToken(String token);

    /**
     * Busca um token de acesso pelo valor do token.
     *
     * @param token Valor do token JWT.
     * @return Optional contendo o TokenAcessoResponse, se encontrado.
     */
    Optional<TokenAcessoResponse> buscarPorToken(String token);

    /**
     * Lista todos os tokens válidos de um determinado usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de TokenAcessoResponse.
     */
    List<TokenAcessoResponse> listarTokensValidosPorUsuario(Long usuarioId);

    /**
     * Remove todos os tokens associados a um usuário.
     *
     * @param usuarioId ID do usuário.
     */
    void deletarPorUsuario(Long usuarioId);
}
