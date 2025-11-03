package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado das informações de um endereço empresarial.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEmpresaResponse {

    private Long id;
    private Long empresaId;
    private String empresaRazaoSocial;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private TipoEnderecoEmpresa tipo;
}