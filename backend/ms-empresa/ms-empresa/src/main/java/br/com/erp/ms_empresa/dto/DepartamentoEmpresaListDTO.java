package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada dos departamentos de uma empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoEmpresaListDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
}