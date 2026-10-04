package br.com.unicos.ms_funcionario.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_funcionario.client.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class FuncionarioRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Consulta do próprio cadastro: exige apenas autenticação.
     */
    static final String CAMINHO_PROPRIO_CADASTRO = "/v1/funcionarios/me";

    /**
     * Prefixo da permissão exigida por recurso. Sub-recursos de {@code /v1/funcionarios} são
     * avaliados antes da rota principal.
     */
    private static final Map<String, String> PREFIXOS = new LinkedHashMap<>();

    static {
        PREFIXOS.put("/v1/funcionarios/cargos", "FUNCIONARIO_CARGO_");
        PREFIXOS.put("/v1/funcionarios", "FUNCIONARIO_");
    }

    private final PermissaoService permissaoService;

    public FuncionarioRequestFilter(
            TokenCoreService tokenCoreService,
            TokenInternoService tokenInternoService,
            PermissaoService permissaoService
    ) {
        super(tokenCoreService, tokenInternoService);
        this.permissaoService = permissaoService;
    }

    @Override
    protected boolean usuarioPossuiPermissao(String permissao) {
        return permissaoService.usuarioPossuiPermissao(permissao);
    }

    @Override
    protected String resolverPermissao(String metodoHttp, String path) {
        if (path.equals(CAMINHO_PROPRIO_CADASTRO) && (metodoHttp.equals("GET") || metodoHttp.equals("HEAD")))
            return null;

        String prefixo = prefixoDoRecurso(PREFIXOS, path);
        return prefixo == null ? null : permissaoPorMetodo(prefixo, metodoHttp);
    }
}
