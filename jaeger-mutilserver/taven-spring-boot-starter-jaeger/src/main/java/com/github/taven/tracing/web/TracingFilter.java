package com.github.taven.tracing.web;

import com.github.taven.tracing.HttpServletRequestExtractAdapter;
import com.github.taven.tracing.common.TracingError;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tianwen.yin
 */
public class TracingFilter implements Filter {
    private final Tracer tracer;

    public TracingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        SpanContext extractedContext = tracer.extract(Format.Builtin.HTTP_HEADERS,
                new HttpServletRequestExtractAdapter(httpRequest));

        String operationName = httpRequest.getMethod() + ":" + httpRequest.getRequestURI();
        Span span = tracer.buildSpan(operationName)
                .asChildOf(extractedContext)
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                .start();

        httpResponse.setHeader("TraceId", span.context().toTraceId());

        try (Scope scope = tracer.activateSpan(span)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            TracingError.handle(span, ex);
            throw ex;
        } finally {
            if (httpRequest.isAsyncStarted()) {
                addAsyncListener(httpRequest, span);
            } else {
                span.finish();
            }
        }
    }

    private void addAsyncListener(HttpServletRequest httpRequest, Span span) {
        httpRequest.getAsyncContext()
                .addListener(new AsyncListener() {
                    @Override
                    public void onComplete(AsyncEvent asyncEvent) throws IOException {
                        span.finish();
                    }

                    @Override
                    public void onTimeout(AsyncEvent asyncEvent) throws IOException {

                    }

                    @Override
                    public void onError(AsyncEvent asyncEvent) throws IOException {
                        Throwable throwable = asyncEvent.getThrowable();
                        TracingError.handle(span, throwable);
                    }

                    @Override
                    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {

                    }
                });
    }

}
