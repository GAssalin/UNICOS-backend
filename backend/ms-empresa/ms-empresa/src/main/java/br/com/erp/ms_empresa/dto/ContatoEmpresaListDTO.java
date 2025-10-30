package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada dos contatos de uma empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContatoEmpresaListDTO {

    private Long id;
    private String nomeContato;
    private String cargo;
    private String telefone;
    private String celular;
    private String email;
}