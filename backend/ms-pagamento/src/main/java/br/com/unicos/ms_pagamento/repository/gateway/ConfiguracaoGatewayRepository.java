package br.com.unicos.ms_pagamento.repository.gateway;

import br.com.unicos.ms_pagamento.enums.TipoGateway;
import br.com.unicos.ms_pagamento.model.gateway.ConfiguracaoGateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pela persistência da entidade {@link ConfiguracaoGateway}.
 * <p>
 * Permite buscar gateways ativos e por tipo.
 */
@Repository
public interface ConfiguracaoGatewayRepository extends JpaRepository<ConfiguracaoGateway, Long> {

    /**
     * Busca todas as configurações de um tipo específico de gateway.
     *
     * @param tipo Tipo do gateway (PAGSEGURO, STRIPE, MERCADOPAGO, etc.).
     * @return Lista de configurações do tipo informado.
     */
    List<ConfiguracaoGateway> findByTipoGateway(TipoGateway tipo);

    /**
     * Retorna todas as configurações ativas.
     *
     * @return Lista de gateways ativos.
     */
    List<ConfiguracaoGateway> findByAtivoTrue();
}
