package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada das filiais de uma empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilialListDTO {

    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String cidade;
    private String uf;
}