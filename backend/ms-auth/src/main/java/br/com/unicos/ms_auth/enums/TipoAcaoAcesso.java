package br.com.unicos.ms_auth.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de ações registradas pela auditoria de acesso.
 * <p>
 * Utilizado para identificar eventos importantes relacionados à autenticação,
 * como logins bem-sucedidos, falhas de login e operações de logout.
 * Permite rastreabilidade e reforço das políticas de segurança do UniCoS.
 */
@Getter
public enum TipoAcaoAcesso {

    /**
     * Login realizado com sucesso pelo usuário.
     */
    LOGIN_SUCESSO("Login realizado com sucesso"),

    /**
     * Tentativa de login falhou, seja por credenciais inválidas
     * ou por outro motivo relacionado à autenticação.
     */
    LOGIN_FALHA("Falha no login"),

    /**
     * Usuário autenticado executou o logout voluntariamente.
     */
    LOGOUT("Logout do usuário");

    private final String descricao;

    TipoAcaoAcesso(String descricao) {
        this.descricao = descricao;
    }
}
