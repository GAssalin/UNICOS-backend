package br.com.unicos.ms_permissao.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_permissao.service.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PermissaoRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Prefixo da permissão exigida por recurso, comparado por segmento de caminho.
     */
    private static final Map<String, String> PREFIXOS = new LinkedHashMap<>();

    static {
        PREFIXOS.put("/v1/permissoes", "PERMISSAO_");
        PREFIXOS.put("/v1/roles", "ROLE_");
        PREFIXOS.put("/v1/role-permissao", "ROLE_PERMISSAO_");
    }

    private final PermissaoService permissaoService;

    public PermissaoRequestFilter(
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
        // Qualquer usuário autenticado pode consultar as próprias permissões (montagem de menus, etc.).
        if ("GET".equals(metodoHttp) && path.matches("/v1/permissoes/minhas/?"))
            return null;

        String prefixo = PREFIXOS.entrySet().stream()
                .filter(entry -> path.equals(entry.getKey()) || path.startsWith(entry.getKey() + "/"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (prefixo == null)
            return null;

        return switch (metodoHttp) {
            case "GET" -> prefixo.concat("LISTAR");
            case "POST" -> prefixo.concat("CRIAR");
            case "PUT", "PATCH" -> prefixo.concat("EDITAR");
            case "DELETE" -> prefixo.concat("EXCLUIR");
            default -> null;
        };
    }
}
