package br.com.unicos.core.web.feign;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.auth.interno.TokenInternoService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;

/**
 * Propaga o contexto da requisição atual nas chamadas Feign entre microserviços:
 * o JWT do usuário (quando houver) e o token interno exigido pelos endpoints {@code /internal/**}.
 */
public class ContextoFeignInterceptor implements RequestInterceptor {

    private final TokenInternoService tokenInternoService;

    public ContextoFeignInterceptor(TokenInternoService tokenInternoService) {
        this.tokenInternoService = tokenInternoService;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(TokenInternoService.HEADER, tokenInternoService.getToken());

        if (AuthContext.isTokenDefined())
            template.header(HttpHeaders.AUTHORIZATION, AuthContext.getToken());
    }
}
