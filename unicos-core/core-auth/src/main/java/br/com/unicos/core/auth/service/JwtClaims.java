package br.com.unicos.core.auth.service;

/**
 * Nomes e valores das claims utilizadas nos tokens emitidos pelo ms-autenticacao.
 */
public final class JwtClaims {

    public static final String USUARIO_ID = "usuarioId";
    public static final String TENANT_ID = "tenantId";
    public static final String USERNAME = "username";
    public static final String TIPO = "typ";

    public static final String TIPO_ACCESS = "access";
    public static final String TIPO_REFRESH = "refresh";

    private JwtClaims() {
    }
}
