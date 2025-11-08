package br.com.unicos.core.pedido.model;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import br.com.unicos.core.pedido.enums.StatusPedido;
import br.com.unicos.core.pedido.enums.TipoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade base que representa um pedido dentro do sistema UniCoS.
 *
 * <p>
 * Esta classe define os atributos essenciais e universais de um pedido,
 * sendo utilizada como modelo compartilhado entre diferentes contextos —
 * como pedidos de compra, venda, ou outras operações que envolvam
 * movimentações comerciais.
 * </p>
 *
 * <p>
 * Os microserviços especializados (como {@code ms-compras} e {@code ms-vendas})
 * podem estender esta entidade para incluir informações adicionais específicas
 * de cada domínio.
 * </p>
 *
 * <p>
 * Mapeada como uma entidade JPA, permite persistência e integração com os
 * bancos de dados de cada serviço, garantindo consistência e compatibilidade.
 * </p>
 */
@Entity
@Table(name = "pedido")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Pedido extends EntidadeAuditavel {

    /**
     * Identificador único do pedido.
     * <p>
     * Gerado automaticamente pela estratégia de incremento do banco de dados.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Status atual do pedido.
     * <p>
     * Define a etapa do fluxo de processamento (ex: ABERTO, APROVADO, FINALIZADO).
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    /**
     * Tipo de pedido.
     * <p>
     * Identifica a origem ou categoria do pedido (ex: COTAÇÃO, REQUISIÇÃO, CONTRATO).
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPedido tipo;

    /**
     * Valor total do pedido.
     * <p>
     * Representa a soma de todos os itens, antes da aplicação de descontos ou acréscimos.
     * </p>
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Data e hora em que o pedido foi criado.
     */
    private LocalDateTime dataCriacao;

    /**
     * Data e hora da última atualização do pedido.
     * <p>
     * É atualizada automaticamente pelos serviços que manipulam esta entidade.
     * </p>
     */
    private LocalDateTime dataAtualizacao;
}
