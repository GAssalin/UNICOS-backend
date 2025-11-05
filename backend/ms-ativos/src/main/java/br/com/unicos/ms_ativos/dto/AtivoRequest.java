package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para requisições de criação ou atualização de ativos.
 * <p>
 * Contém os dados essenciais para o cadastro e manutenção de um ativo.
 */
public record AtivoRequest(

        /** Nome identificador do ativo. */
        @NotBlank(message = "O nome do ativo é obrigatório.")
        String nome,

        /** Código patrimonial único utilizado para controle interno. */
        @NotBlank(message = "O código patrimonial é obrigatório.")
        String codigoPatrimonial,

        /** Descrição detalhada do ativo. */
        String descricao,

        /** Tipo do ativo (MÓVEL, IMÓVEL, VEÍCULO, etc.). */
        @NotNull(message = "O tipo do ativo é obrigatório.")
        TipoAtivo tipo,

        /** Status atual do ativo (ATIVO, EM_MANUTENCAO, INATIVO, etc.). */
        @NotNull(message = "O status do ativo é obrigatório.")
        StatusAtivo status,

        /** Data de aquisição do ativo. */
        @NotNull(message = "A data de aquisição é obrigatória.")
        @PastOrPresent(message = "A data de aquisição não pode estar no futuro.")
        LocalDate dataAquisicao,

        /** Valor de aquisição do ativo. */
        @NotNull(message = "O valor de aquisição é obrigatório.")
        @DecimalMin(value = "0.0", message = "O valor de aquisição deve ser positivo.")
        BigDecimal valorAquisicao,

        /** Valor atual contábil do ativo (após depreciação). */
        @DecimalMin(value = "0.0", message = "O valor atual deve ser positivo.")
        BigDecimal valorAtual,

        /** Identificador da empresa proprietária (referência ao ms-empresa). */
        @NotNull(message = "O ID da empresa é obrigatório.")
        Long empresaId,

        /** Identificador da filial onde o ativo está alocado. */
        @NotNull(message = "O ID da filial é obrigatório.")
        Long filialId,

        /** Identificador do colaborador responsável (referência ao ms-pessoas). */
        Long responsavelId,

        /** Identificador da localização física do ativo (referência ao ms-ativos). */
        Long localizacaoId
) { }
