package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa as categorias de ativos que podem ser cadastradas no sistema.
 * <p>
 * Cada tipo indica a natureza e finalidade do ativo dentro da organização.
 */
@Getter
public enum TipoAtivo {

    /**
     * Equipamentos utilizados nas operações da empresa.
     */
    EQUIPAMENTO("Equipamento"),

    /**
     * Veículos pertencentes ao patrimônio da empresa.
     */
    VEICULO("Veículo"),

    /**
     * Mobiliários e itens de escritório.
     */
    MOBILIARIO("Mobiliário"),

    /**
     * Softwares licenciados ou desenvolvidos internamente.
     */
    SOFTWARE("Software"),

    /**
     * Imóveis próprios ou alugados utilizados pela empresa.
     */
    IMOVEL("Imóvel"),

    /**
     * Categoria genérica para ativos não enquadrados nas demais opções.
     */
    OUTROS("Outros");

    private final String descricao;

    TipoAtivo(String descricao) {
        this.descricao = descricao;
    }
}
