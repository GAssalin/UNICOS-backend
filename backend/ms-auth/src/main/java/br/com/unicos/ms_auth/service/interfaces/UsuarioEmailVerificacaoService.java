package br.com.unicos.ms_auth.service.interfaces;

/**
 * Serviço responsável pelo fluxo de verificação de e-mail do usuário.
 */
public interface UsuarioEmailVerificacaoService {

    /**
     * Gera um token de verificação e o associa ao usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Token real (não hasheado) para envio por e-mail.
     */
    String gerarTokenParaUsuario(Long usuarioId);

    /**
     * Reenvia um token, invalidando o anterior se existir.
     *
     * @param usuarioId ID do usuário.
     * @return Token real (não hasheado) para envio por e-mail.
     */
    String reenviarToken(Long usuarioId);

    /**
     * Confirma o e-mail de um usuário através do token informado.
     *
     * @param token Token enviado ao usuário.
     */
    void confirmarEmail(String token);

    /**
     * Marca tokens expirados como utilizados.
     */
    void limparTokensExpirados();
}
