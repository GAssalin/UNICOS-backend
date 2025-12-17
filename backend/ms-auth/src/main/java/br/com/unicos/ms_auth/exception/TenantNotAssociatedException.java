package br.com.unicos.ms_auth.exception;

public class TenantNotAssociatedException extends RuntimeException {
    public TenantNotAssociatedException() {
        super("Usuário não possui empresa associada");
    }
}
