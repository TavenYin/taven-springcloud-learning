package com.github.taven.order.tracing;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

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

    public static class TracingRestTemplateInterceptor implements ClientHttpRequestInterceptor {
        private final Tracer tracer;

        public TracingRestTemplateInterceptor(Tracer tracer) {
            this.tracer = tracer;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            ClientHttpResponse httpResponse;
            // 为当前 RestTemplate 调用，创建一个 Span
            Span span = tracer.buildSpan("RestTemplate-RPC")
                    .withTag(Tags.SPAN_KIND.getKey(), "order-server")
                    .start();
            // 将当前 SpanContext 注入到 HttpHeaders
            tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS,
                    new SpringHttpHeadersCarrier(request.getHeaders()));

            try (Scope scope = tracer.activateSpan(span)) {
                httpResponse = execution.execute(request, body);
            } finally {
                span.finish();
            }
            return httpResponse;
        }
    }
}
