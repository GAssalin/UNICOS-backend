package br.com.unicos.ms_estoque.filter;

import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.auth.service.TokenCoreService;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import br.com.unicos.ms_estoque.client.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EstoqueRequestFilter extends ContextoRequisicaoFilter {

    /**
     * Prefixo da permissão exigida por recurso. A comparação é feita por segmento de caminho,
     * de modo que {@code /v1/estoques} não captura {@code /v1/estoques-produtos}.
     */
    private static final Map<String, String> PREFIXOS = new LinkedHashMap<>();

    static {
        PREFIXOS.put("/v1/estoques", "ESTOQUE_");
        PREFIXOS.put("/v1/estoques-produtos", "ESTOQUE_PRODUTO_");
        PREFIXOS.put("/v1/movimentacoes-estoque", "ESTOQUE_MOVIMENTACAO_");
        PREFIXOS.put("/v1/movimentacoes-estoque-itens", "ESTOQUE_MOVIMENTACAO_");
        PREFIXOS.put("/v1/responsaveis-estoque", "ESTOQUE_RESPONSAVEL_");
        PREFIXOS.put("/v1/vinculos-estoque-filial", "ESTOQUE_VINCULO_FILIAL_");
    }

    private final PermissaoService permissaoService;

    public EstoqueRequestFilter(
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
