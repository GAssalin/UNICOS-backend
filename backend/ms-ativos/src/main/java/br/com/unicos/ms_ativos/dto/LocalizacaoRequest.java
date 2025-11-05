package br.com.unicos.ms_ativos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação ou atualização de localizações físicas
 * dentro de uma unidade (filial).
 */
public record LocalizacaoRequest(

        /** Descrição da localização (ex: "Sala de Servidores", "Depósito Central"). */
        @NotBlank(message = "A descrição da localização é obrigatória.")
        String descricao,

        /** Identificação do andar (ex: "Térreo", "1º", "2º"). */
        String andar,

        /** Identificação do bloco ou prédio (ex: "A", "B", "Administrativo"). */
        String bloco,

        /** Identificador da filial onde a localização pertence (referência ao ms-empresa). */
        @NotNull(message = "O ID da filial é obrigatório.")
        Long filialId
) { }
