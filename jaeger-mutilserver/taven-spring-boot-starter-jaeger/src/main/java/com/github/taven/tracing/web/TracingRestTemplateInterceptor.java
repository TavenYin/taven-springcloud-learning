package com.github.taven.tracing.web;

import com.github.taven.tracing.HttpHeadersCarrier;
import com.github.taven.tracing.common.TracingError;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author tianwen.yin
 */
public class TracingRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static final String SPAN_URI = "uri";

    private final Tracer tracer;

    public TracingRestTemplateInterceptor(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse httpResponse;
        // 为当前 RestTemplate 调用，创建一个 Span
        Span span = tracer.buildSpan("RestTemplate-RPC")
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                .withTag(SPAN_URI, request.getURI().toString())
                .start();
        // 将当前 SpanContext 注入到 HttpHeaders
        tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS,
                new HttpHeadersCarrier(request.getHeaders()));

        try (Scope scope = tracer.activateSpan(span)) {
            httpResponse = execution.execute(request, body);
        } catch (Exception ex) {
            TracingError.handle(span, ex);
            throw ex;
        } finally {
            span.finish();
        }
        return httpResponse;
    }

}
