package br.com.unicos.ms_empresa.filter;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_empresa.client.PermissaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmpresaRequestFilter extends OncePerRequestFilter {

    private final PermissaoService permissaoService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String usuarioId = request.getHeader("X-Usuario-Id");
            String tenantId = request.getHeader("X-Tenant-Id");

            if (usuarioId != null && tenantId != null) {

                UserContext.setUsuarioId(Long.valueOf(usuarioId));
                TenantContext.setEmpresaId(Long.valueOf(tenantId));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                Long.valueOf(usuarioId),
                                null,
                                List.of()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                validarPermissaoPorRota(request);
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
            TenantContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private void validarPermissaoPorRota(HttpServletRequest request) {
        String permissao = resolverPermissao(request.getMethod(), request.getServletPath());

        if (permissao == null)
            return;
        if (!permissaoService.usuarioPossuiPermissao(permissao))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não possui permissão para acessar este recurso.");
    }

    private String resolverPermissao(String metodoHttp, String path) {
        String prefixo;

        if (path.startsWith("/v1/empresas/configuracoes"))
            prefixo = "EMPRESA_CONFIGURACAO_";
        else if (path.startsWith("/v1/empresas/contatos"))
            prefixo = "EMPRESA_CONTATO_";
        else if (path.startsWith("/v1/empresas/enderecos"))
            prefixo = "EMPRESA_ENDERECO_";
        else if (path.startsWith("/v1/empresas/parametros"))
            prefixo = "EMPRESA_PARAMETRO_";
        else if (path.startsWith("/v1/empresas/usuarios"))
            prefixo = "EMPRESA_USUARIO_";
        else if (path.startsWith("/v1/empresas"))
            prefixo = "EMPRESA_";
        else
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