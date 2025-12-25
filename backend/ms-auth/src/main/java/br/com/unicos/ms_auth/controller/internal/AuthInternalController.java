package br.com.unicos.ms_auth.controller.internal;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.repository.RolePermissaoRepository;
import br.com.unicos.ms_auth.security_access.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class AuthInternalController {

    private final TokenService tokenService;
    private final RolePermissaoRepository rolePermissaoRepository;

    @PostMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer "))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String token = authorization.substring(7);

        DecodedJWT decodedJWT;
        try {
            decodedJWT = tokenService.verificarAccessToken(token);
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Long usuarioId = decodedJWT.getClaim("usuarioId").asLong();
        Long empresaId = decodedJWT.getClaim("tenantId").asLong();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        if (usuarioId == null || empresaId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        TokenValidationResponse response = new TokenValidationResponse(
                usuarioId,
                empresaId,
                roles != null ? Set.copyOf(roles) : Set.of()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/permissions/check")
    public boolean usuarioPossuiPermissao(@RequestParam String nomePermissao) {
        return rolePermissaoRepository.rolePossuiPermissao(TenantContext.getEmpresaId(), AuthContext.getRoles().stream().toList(), nomePermissao);
    }
}
