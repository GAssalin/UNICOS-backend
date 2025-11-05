package br.com.unicos.ms_ativos.dto;

/**
 * DTO simplificado para listagem de fornecedores de manutenção.
 * <p>
 * Usado em consultas de tabela e relatórios resumidos.
 */
public record FornecedorManutencaoListDTO(

        /** Identificador único do fornecedor. */
        Long id,

        /** Nome ou razão social do fornecedor. */
        String nome,

        /** CNPJ do fornecedor. */
        String cnpj,

        /** Telefone de contato principal. */
        String telefone,

        /** E-mail principal. */
        String email
) { }
