package br.com.unicos.ms_auth.service.interfaces;

import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoRequest;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResponse;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio relacionadas
 * ao vínculo entre Empresa, Role e Permissão.
 *
 * <p>
 * Este serviço é a base do controle de autorização multi-tenant
 * do UniCoS.
 */
public interface EmpresaRolePermissaoService {

    /**
     * Cria um novo vínculo entre empresa, role e permissão.
     */
    EmpresaRolePermissaoResponse criar(EmpresaRolePermissaoRequest request);

    /**
     * Ativa ou desativa um vínculo existente.
     */
    EmpresaRolePermissaoResponse alterarStatus(Long id, Boolean ativo);

    /**
     * Remove definitivamente um vínculo.
     */
    void remover(Long id);

    /**
     * Lista todos os vínculos de uma empresa (ativos e inativos).
     */
    List<EmpresaRolePermissaoListDTO> listarPorEmpresa(Long empresaId);

    /**
     * Lista apenas vínculos ativos de uma empresa.
     */
    List<EmpresaRolePermissaoListDTO> listarAtivosPorEmpresa(Long empresaId);

}
