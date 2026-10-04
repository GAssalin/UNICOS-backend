package br.com.unicos.ms_pessoas.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_pessoas.client.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PessoaRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Prefixo da permissão exigida por recurso. A comparação é feita por segmento de caminho,
     * de modo que {@code /v1/pessoas} não captura {@code /v1/pessoas-fisicas}.
     */
    private static final Map<String, String> PREFIXOS = new LinkedHashMap<>();

    static {
        PREFIXOS.put("/v1/contatos", "PESSOA_CONTATO_");
        PREFIXOS.put("/v1/documentos", "PESSOA_DOCUMENTO_");
        PREFIXOS.put("/v1/enderecos", "PESSOA_ENDERECO_");
        PREFIXOS.put("/v1/municipios", "PESSOA_MUNICIPIO_");
        PREFIXOS.put("/v1/pessoas", "PESSOA_");
        PREFIXOS.put("/v1/pessoas-fisicas", "PESSOA_FISICA_");
        PREFIXOS.put("/v1/pessoas-juridicas", "PESSOA_JURIDICA_");
        PREFIXOS.put("/v1/pessoas-relacoes", "PESSOA_RELACAO_");
        PREFIXOS.put("/v1/tipos-relacao-pessoa", "PESSOA_TIPO_RELACAO_PESSOA_");
        PREFIXOS.put("/v1/usuarios", "USUARIO_");
        PREFIXOS.put("/v1/verificacao-email", "USUARIO_EMAIL_");
    }

    private final PermissaoService permissaoService;

    public PessoaRequestFilter(
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
        if ("PATCH".equals(metodoHttp) && path.matches("/v1/usuarios/[^/]+/ativar/?"))
            return "USUARIO_ATIVAR";
        if ("PATCH".equals(metodoHttp) && path.matches("/v1/usuarios/[^/]+/desativar/?"))
            return "USUARIO_DESATIVAR";

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
