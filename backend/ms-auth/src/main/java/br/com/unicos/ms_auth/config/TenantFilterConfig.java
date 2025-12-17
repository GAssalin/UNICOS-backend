package br.com.unicos.ms_auth.config;

import br.com.unicos.core.tenant.filter.TenantContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class TenantFilterConfig {

    @Bean
    public FilterRegistrationBean<TenantContextFilter> tenantContextFilter() {
        FilterRegistrationBean<TenantContextFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new TenantContextFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}
