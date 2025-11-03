package br.com.unicos.ms_empresa.dto;

/**
 * DTO usado para listagem simplificada dos contatos de uma empresa.
 */
public record ContatoEmpresaListDTO(
        Long id,
        String nomeContato,
        String cargo,
        String telefone,
        String celular,
        String email
) {}