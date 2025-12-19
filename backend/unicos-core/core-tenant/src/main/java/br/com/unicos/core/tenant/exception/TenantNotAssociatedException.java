package br.com.unicos.core.tenant.exception;

public class TenantNotAssociatedException extends RuntimeException {
    public TenantNotAssociatedException() {
        super("Usuário não possui empresa associada");
    }
}
