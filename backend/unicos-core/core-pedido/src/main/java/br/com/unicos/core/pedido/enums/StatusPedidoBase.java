package br.com.unicos.core.pedido.enums;

/**
 * Interface base para todos os status de pedidos no sistema UniCoS.
 *
 * <p>
 * Fornece um contrato comum para os diferentes tipos de status,
 * permitindo que os módulos especializados (como Compras e Vendas)
 * implementem suas próprias variações mantendo compatibilidade.
 * </p>
 */
public interface StatusPedidoBase {

    /**
     * Retorna a descrição legível do status.
     *
     * @return descrição textual do status.
     */
    String getDescricao();

}
