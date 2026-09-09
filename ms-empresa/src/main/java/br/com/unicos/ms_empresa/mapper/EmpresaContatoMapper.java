package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoListDTO;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import org.springframework.stereotype.Component;


/**
 * Mapper responsável pela conversão entre a entidade {@link EmpresaContato}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * garantindo clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaContatoMapper {

    /**
     * Converte o DTO de criação para uma nova entidade {@link EmpresaContato}.
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaContato
     */
    public EmpresaContato toEntity(EmpresaContatoCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaContato entity = new EmpresaContato();
        entity.setTipoContato(request.tipoContato());
        entity.setValor(request.valor());
        entity.setPrincipal(request.principal());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaContato}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id}
     * e {@code tipoContato}, não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntity(EmpresaContato entity, EmpresaContatoUpdateRequest request) {
        if (request == null || entity == null) {
            return;
        }

        entity.setValor(request.valor());
        entity.setPrincipal(request.principal());
    }

    /**
     * Converte a entidade {@link EmpresaContato} para o DTO de resposta detalhada.
     *
     * @param entity entidade de contato
     * @return DTO detalhado do contato
     */
    public EmpresaContatoResponse toResponse(EmpresaContato entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaContatoResponse(
                entity.getId(),
                entity.getTipoContato(),
                entity.getValor(),
                entity.isPrincipal(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte a entidade {@link EmpresaContato} para o DTO resumido.
     *
     * @param entity entidade de contato
     * @return DTO resumido do contato
     */
    public EmpresaContatoListDTO toListDTO(EmpresaContato entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaContatoListDTO(
                entity.getId(),
                entity.getTipoContato(),
                entity.getValor(),
                entity.isPrincipal()
        );
    }




}