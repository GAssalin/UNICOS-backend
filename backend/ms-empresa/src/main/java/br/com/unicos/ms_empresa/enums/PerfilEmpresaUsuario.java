package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa os perfis de acesso do usuário dentro da empresa.
 * <p>
 * Define permissões e responsabilidades no contexto do tenant.
 */
@Getter
public enum PerfilEmpresaUsuario {

    /**
     * Perfil administrador com acesso total à empresa.
     */
    ADMIN("Administrador"),

    /**
     * Perfil responsável por operações financeiras.
     */
    FINANCEIRO("Financeiro"),

    /**
     * Perfil operacional para atividades do dia a dia.
     */
    OPERACIONAL("Operacional");

    private final String descricao;

    PerfilEmpresaUsuario(String descricao) {
        this.descricao = descricao;
    }
}
