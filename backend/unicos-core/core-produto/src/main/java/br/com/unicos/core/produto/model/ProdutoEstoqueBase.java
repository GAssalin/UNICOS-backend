package br.com.unicos.core.produto.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo base que representa as informações essenciais de estoque de um produto.
 *
 * <p>
 * Este modelo é utilizado entre microserviços para operações relacionadas a movimentação,
 * reserva, baixa, disponibilidade e cálculo de saldo em depósitos.
 * </p>
 *
 * <p>
 * Ele é fundamental para integração consistente entre módulos como:
 * <b>ms-estoque</b>, <b>ms-vendas</b>, <b>ms-compras</b> e <b>ms-produtos</b>.
 * </p>
 *
 * <p>
 * Não é uma entidade JPA e não deve ser persistido diretamente. Seu papel é
 * exclusivamente transportar dados padronizados sobre estoque no ecossistema UNICOS.
 * </p>
 */
@Embeddable
public class ProdutoEstoqueBase {

    /**
     * Identificador do depósito onde o estoque está alocado.
     */
    @Transient
    private Long depositoId;

    /**
     * Unidade de medida utilizada para representar a quantidade do produto.
     */
    @Transient
    private String unidadeMedida;

    /**
     * Quantidade disponível para venda ou movimentação.
     */
    @Transient
    private BigDecimal quantidadeDisponivel;

    /**
     * Quantidade reservada para pedidos já confirmados.
     */
    @Transient
    private BigDecimal quantidadeReservada;

    /**
     * Quantidade total (disponível + reservada).
     */
    @Transient
    private BigDecimal quantidadeTotal;

    /**
     * Data e hora da última atualização do saldo.
     */
    @Transient
    private LocalDateTime ultimaAtualizacao;

    public ProdutoEstoqueBase() {
    }

    public ProdutoEstoqueBase(
            Long depositoId,
            String unidadeMedida,
            BigDecimal quantidadeDisponivel,
            BigDecimal quantidadeReservada,
            BigDecimal quantidadeTotal,
            LocalDateTime ultimaAtualizacao
    ) {
        this.depositoId = depositoId;
        this.unidadeMedida = unidadeMedida;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeReservada = quantidadeReservada;
        this.quantidadeTotal = quantidadeTotal;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Long getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(Long depositoId) {
        this.depositoId = depositoId;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(BigDecimal quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public BigDecimal getQuantidadeReservada() {
        return quantidadeReservada;
    }

    public void setQuantidadeReservada(BigDecimal quantidadeReservada) {
        this.quantidadeReservada = quantidadeReservada;
    }

    public BigDecimal getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(BigDecimal quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
