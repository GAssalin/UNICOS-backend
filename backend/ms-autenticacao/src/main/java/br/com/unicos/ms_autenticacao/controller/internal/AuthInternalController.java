package br.com.unicos.ms_autenticacao.controller.internal;

import br.com.unicos.core.auth.dto.TokenValidationResponse;
import br.com.unicos.ms_autenticacao.service.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/autenticacao")
@RequiredArgsConstructor
public class AuthInternalController {

    private final TokenService tokenService;

    @PostMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer "))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String token = authorization.substring(7).trim();

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

        if (usuarioId == null || empresaId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        TokenValidationResponse response = new TokenValidationResponse(
                usuarioId,
                empresaId
        );

        return ResponseEntity.ok(response);
    }

}
