package com.github.taven.tracing.config;

import com.github.taven.tracing.web.TracingRestTemplateInterceptor;
import io.opentracing.Tracer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author tianwen.yin
 */
@Configuration
public class TracingRestTemplateConfiguration {
    private final Tracer tracer;
    private final List<RestTemplate> restTemplates;

    public TracingRestTemplateConfiguration(Tracer tracer, List<RestTemplate> restTemplates) {
        this.tracer = tracer;
        this.restTemplates = restTemplates;
        registerTracingInterceptor();
    }

    private void registerTracingInterceptor() {
        for (RestTemplate restTemplate : restTemplates) {
            restTemplate.getInterceptors()
                    .add(new TracingRestTemplateInterceptor(tracer));
        }
    }

}
