package br.com.unicos.ms_cliente.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_cliente.client.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ClienteRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Prefixo da permissão exigida por recurso. Sub-recursos de {@code /v1/clientes} são avaliados
     * antes da rota principal.
     */
    private static final Map<String, String> PREFIXOS = new LinkedHashMap<>();

    static {
        PREFIXOS.put("/v1/clientes/categorias", "CLIENTE_CATEGORIA_");
        PREFIXOS.put("/v1/clientes/observacoes", "CLIENTE_OBSERVACAO_");
        PREFIXOS.put("/v1/clientes", "CLIENTE_");
    }

    private final PermissaoService permissaoService;

    public ClienteRequestFilter(
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
