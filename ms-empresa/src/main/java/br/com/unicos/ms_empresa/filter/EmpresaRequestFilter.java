package br.com.unicos.ms_empresa.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_empresa.client.PermissaoService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmpresaRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Sub-recursos de {@code /v1/empresas/{empresaRefId}/...} e o prefixo da permissão de cada um.
     */
    private static final Map<String, String> SUB_RECURSOS = new LinkedHashMap<>();

    private static final Pattern SUB_RECURSO = Pattern.compile("/v1/empresas/([^/]+)/([^/]+)(?:/.*)?");

    static {
        SUB_RECURSOS.put("configuracoes", "EMPRESA_CONFIGURACAO_");
        SUB_RECURSOS.put("contatos", "EMPRESA_CONTATO_");
        SUB_RECURSOS.put("enderecos", "EMPRESA_ENDERECO_");
        SUB_RECURSOS.put("parametros", "EMPRESA_PARAMETRO_");
        SUB_RECURSOS.put("usuarios", "EMPRESA_USUARIO_");
    }

    private final PermissaoService permissaoService;

    public EmpresaRequestFilter(
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
        // Mantém o alias legado sob as mesmas permissões da rota versionada.
        if (path.equals("/api/empresas") || path.startsWith("/api/empresas/"))
            path = "/v1/empresas" + path.substring("/api/empresas".length());

        String prefixo = resolverPrefixo(path);

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

    private static String resolverPrefixo(String path) {
        Matcher matcher = SUB_RECURSO.matcher(path);

        if (matcher.matches() && SUB_RECURSOS.containsKey(matcher.group(2))) {
            validarEmpresaDoCaminho(matcher.group(1));
            return SUB_RECURSOS.get(matcher.group(2));
        }

        if (path.equals("/v1/empresas") || path.startsWith("/v1/empresas/"))
            return "EMPRESA_";

        return null;
    }

    /**
     * Os sub-recursos sempre operam sobre a empresa do usuário autenticado; o identificador do
     * caminho precisa corresponder a ela.
     */
    private static void validarEmpresaDoCaminho(String empresaRefId) {
        if (!TenantContext.getEmpresaId().toString().equals(empresaRefId))
            throw new AccessDeniedException("Acesso negado aos dados de outra empresa.");
    }
}
