package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que define as categorias gerais de ativos.
 */
@Getter
public enum TipoAtivo {

    /**
     * Equipamentos eletrônicos e tecnológicos.
     */
    EQUIPAMENTO("Equipamento eletrônico ou tecnológico"),

    /**
     * Veículos automotores.
     */
    VEICULO("Veículo automotor"),

    /**
     * Softwares licenciados ou desenvolvidos internamente.
     */
    SOFTWARE("Software"),

    /**
     * Imóveis (prédios, terrenos, salas, etc.).
     */
    IMOVEL("Bem imóvel"),

    /**
     * Móveis e utensílios de escritório.
     */
    MOVEL("Móvel ou utensílio de escritório"),

    /**
     * Ferramentas e instrumentos de trabalho.
     */
    FERRAMENTA("Ferramenta ou instrumento de trabalho"),

    /**
     * Outro tipo de ativo não classificado.
     */
    OUTRO("Outro tipo de ativo");

    private final String descricao;

    TipoAtivo(String descricao) {
        this.descricao = descricao;
    }
}
