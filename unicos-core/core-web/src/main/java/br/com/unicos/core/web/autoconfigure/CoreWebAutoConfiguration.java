package br.com.unicos.core.web.autoconfigure;

import br.com.unicos.core.auth.autoconfigure.CoreAuthAutoConfiguration;
import br.com.unicos.core.auth.interno.TokenInternoService;
import br.com.unicos.core.web.error.TratadorExcecoesFeign;
import br.com.unicos.core.web.error.TratadorExcecoesJpa;
import br.com.unicos.core.web.error.TratadorExcecoesPadrao;
import br.com.unicos.core.web.error.TratadorExcecoesSeguranca;
import br.com.unicos.core.web.feign.ContextoFeignInterceptor;
import br.com.unicos.core.web.filter.ContextoRequisicaoFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuração dos componentes web compartilhados pelos microserviços servlet.
 */
@AutoConfiguration(after = CoreAuthAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CoreWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TratadorExcecoesPadrao tratadorExcecoesPadrao() {
        return new TratadorExcecoesPadrao();
    }

    /**
     * O filtro de contexto é declarado como {@code @Component} em cada microserviço e adicionado ao
     * {@code SecurityFilterChain}. Este registro impede que o Spring Boot também o registre
     * diretamente no container servlet, o que o faria executar fora da cadeia de segurança.
     */
    @Bean
    @ConditionalOnBean(ContextoRequisicaoFilter.class)
    public FilterRegistrationBean<ContextoRequisicaoFilter> contextoRequisicaoFilterRegistration(
            ContextoRequisicaoFilter filter
    ) {
        FilterRegistrationBean<ContextoRequisicaoFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "jakarta.persistence.EntityNotFoundException")
    static class JpaConfiguration {

        @Bean
        @ConditionalOnMissingBean
        TratadorExcecoesJpa tratadorExcecoesJpa() {
            return new TratadorExcecoesJpa();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.security.access.AccessDeniedException")
    static class SegurancaConfiguration {

        @Bean
        @ConditionalOnMissingBean
        TratadorExcecoesSeguranca tratadorExcecoesSeguranca() {
            return new TratadorExcecoesSeguranca();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "feign.FeignException")
    static class FeignConfiguration {

        @Bean
        @ConditionalOnMissingBean
        TratadorExcecoesFeign tratadorExcecoesFeign() {
            return new TratadorExcecoesFeign();
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnBean(TokenInternoService.class)
        ContextoFeignInterceptor contextoFeignInterceptor(TokenInternoService tokenInternoService) {
            return new ContextoFeignInterceptor(tokenInternoService);
        }
    }
}
