package br.com.unicos.ms_usuario.security_access;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ServiceAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public ServiceAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
    }
}