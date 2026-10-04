package br.com.unicos.ms_produto.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_produto.client.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ProdutoRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Prefixo da permissão exigida por recurso. Sub-recursos de {@code /v1/produtos} são avaliados
     * antes da rota principal.
     */
    private static final Map<String, String> PREFIXOS = new LinkedHashMap<>();

    static {
        PREFIXOS.put("/v1/produtos/categorias", "PRODUTO_CATEGORIA_");
        PREFIXOS.put("/v1/produtos/marcas-produto", "PRODUTO_MARCAS_PRODUTO_");
        PREFIXOS.put("/v1/produtos/atributos-valores", "PRODUTO_ATRIBUTOS_VALORES_");
        PREFIXOS.put("/v1/produtos/atributos", "PRODUTO_ATRIBUTOS_");
        PREFIXOS.put("/v1/produtos/codigo-barras", "PRODUTO_CODIGO_BARRAS_");
        PREFIXOS.put("/v1/produtos/imagens", "PRODUTO_IMAGENS_");
        PREFIXOS.put("/v1/produtos/precos-base", "PRODUTO_PRECO_BASE_");
        PREFIXOS.put("/v1/produtos/tipos", "PRODUTO_TIPOS_");
        PREFIXOS.put("/v1/produtos/unidades-medida", "PRODUTO_UNIDADE_MEDIDA_");
        PREFIXOS.put("/v1/produtos", "PRODUTO_");
    }

    private final PermissaoService permissaoService;

    public ProdutoRequestFilter(
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
        String prefixo = prefixoDoRecurso(PREFIXOS, path);
        return prefixo == null ? null : permissaoPorMetodo(prefixo, metodoHttp);
    }
}
