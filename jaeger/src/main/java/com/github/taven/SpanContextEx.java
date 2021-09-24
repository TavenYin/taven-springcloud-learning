package com.github.taven;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * 单线程如何传递 Span 上下文
 *
 * 跨服务传递 Span，使用 tracer.inject  和 tracer.extract
 */
public class SpanContextEx {

    private final Tracer tracer;

    private SpanContextEx(Tracer tracer) {
        this.tracer = tracer;
    }

    private void sayHello(String helloTo) {
        Span span = tracer.buildSpan("hello-opentracing").start();

        try (Scope scope = tracer.scopeManager().activate(span)) {
            // 增加Tags信息
            span.log("SpanContextEx");
            innerSpan();
            innerSpan2();
        } finally {
            span.finish();
        }

        System.out.println("Trace Id: " + span.context().toTraceId());
    }

    private void innerSpan() {
        Span span = tracer.buildSpan("inner-span").start();

        try (Scope scope = tracer.scopeManager().activate(span)) {
            // 增加Tags信息
            span.log("SpanContextEx");
        } finally {
            span.finish();
        }
    }

    private void innerSpan2() {
        Span span = tracer.buildSpan("inner-span2").start();

        try (Scope scope = tracer.scopeManager().activate(span)) {
            // 增加Tags信息
            span.log("SpanContextEx");
        } finally {
            span.finish();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expecting one argument");
        }
        String helloTo = args[0];

        Tracer tracer = initTracer("hello-world");
        new SpanContextEx(tracer).sayHello(helloTo);
    }

    public static JaegerTracer initTracer(String service) {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
        Configuration config = new Configuration(service).withSampler(samplerConfig).withReporter(reporterConfig);
        return config.getTracer();
    }
}
