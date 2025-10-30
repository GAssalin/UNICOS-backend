package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.model.TipoEnderecoEmpresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para listagem simplificada dos endereços de uma empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEmpresaListDTO {

    private Long id;
    private String logradouro;
    private String numero;
    private String cidade;
    private String uf;
    private TipoEnderecoEmpresa tipo;
}