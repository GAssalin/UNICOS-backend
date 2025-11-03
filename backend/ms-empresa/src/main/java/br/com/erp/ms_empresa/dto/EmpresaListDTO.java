package br.com.erp.ms_empresa.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada de empresas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaListDTO {

    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
}