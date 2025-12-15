package br.com.unicos.core.produto.model;

import br.com.unicos.core.produto.enums.SituacaoTributariaProduto;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Modelo base que representa informações tributárias essenciais do produto.
 *
 * <p>
 * Este modelo padroniza as comunicações entre microserviços que dependem
 * de dados fiscais simplificados, como:
 * <b>ms-compras</b>, <b>ms-vendas</b>, <b>ms-faturamento</b> e módulos
 * relacionados ao cálculo tributário.
 * </p>
 *
 * <p>
 * Observação: códigos como NCM, CEST e CFOP são representados como Strings,
 * pois suas descrições e tabelas completas pertencem ao <b>ms-empresa</b>
 * e/ou <b>ms-fiscal</b>, evitando duplicação e garantindo consistência.
 * </p>
 *
 * <p>
 * Este modelo não deve ser persistido diretamente — é apenas um DTO base
 * de transporte no ecossistema UNICOS.
 * </p>
 */
@Embeddable
public class ProdutoTributacaoBase {

    /**
     * Código NCM (Nomenclatura Comum do Mercosul) do produto.
     */
    private String ncm;

    /**
     * Código CEST associado ao produto.
     */
    private String cest;

    /**
     * Situação tributária aplicável ao produto.
     */
    @Enumerated(EnumType.STRING)
    private SituacaoTributariaProduto situacaoTributaria;

    public ProdutoTributacaoBase() {
    }

    public ProdutoTributacaoBase(String ncm, String cest, SituacaoTributariaProduto situacaoTributaria) {
        this.ncm = ncm;
        this.cest = cest;
        this.situacaoTributaria = situacaoTributaria;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public SituacaoTributariaProduto getSituacaoTributaria() {
        return situacaoTributaria;
    }

    public void setSituacaoTributaria(SituacaoTributariaProduto situacaoTributaria) {
        this.situacaoTributaria = situacaoTributaria;
    }
}
