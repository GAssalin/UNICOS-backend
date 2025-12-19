package br.com.unicos.ms_auth.exception;

import br.com.unicos.core.tenant.exception.TenantNotAssociatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TenantNotAssociatedException.class)
    public ResponseEntity<Void> handleTenantNotAssociated() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}