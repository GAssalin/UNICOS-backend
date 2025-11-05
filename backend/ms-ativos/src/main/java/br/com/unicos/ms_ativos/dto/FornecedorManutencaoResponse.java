package br.com.unicos.ms_ativos.dto;

import java.util.List;

/**
 * DTO de resposta utilizado para exibir informações detalhadas
 * de um fornecedor de manutenção e suas manutenções associadas.
 */
public record FornecedorManutencaoResponse(

        /** Identificador único do fornecedor. */
        Long id,

        /** Nome ou razão social do fornecedor. */
        String nome,

        /** CNPJ do fornecedor. */
        String cnpj,

        /** Telefone de contato. */
        String telefone,

        /** E-mail principal. */
        String email,

        /** Nome do responsável técnico ou comercial. */
        String responsavel,

        /** Lista resumida de manutenções associadas ao fornecedor. */
        List<ManutencaoAtivoListDTO> manutencoes
) { }
