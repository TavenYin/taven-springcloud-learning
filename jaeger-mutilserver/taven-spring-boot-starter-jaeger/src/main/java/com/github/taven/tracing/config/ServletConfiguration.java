package com.github.taven.tracing.config;

import com.github.taven.tracing.web.TracingFilter;
import io.opentracing.Tracer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tianwen.yin
 */
@Configuration
public class ServletConfiguration {

    @Bean
    public FilterRegistrationBean<TracingFilter> tracingFilter(Tracer tracer) {
        TracingFilter tracingFilter = new TracingFilter(tracer);
        FilterRegistrationBean<TracingFilter> filterRegistrationBean = new FilterRegistrationBean<>(tracingFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.setAsyncSupported(true);
        return filterRegistrationBean;
    }

}
