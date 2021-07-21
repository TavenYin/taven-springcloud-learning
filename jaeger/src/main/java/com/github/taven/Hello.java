package com.github.taven;

import com.google.common.collect.ImmutableMap;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.JaegerTracer;

public class Hello {

    private final Tracer tracer;

    private Hello(Tracer tracer) {
        this.tracer = tracer;
    }

    private void sayHello(String helloTo) {
        Span span = tracer.buildSpan("say-hello").start();
        // 增加Tags信息
        span.setTag("hello-to", helloTo);

        String helloStr = String.format("Hello, %s!", helloTo);
        // 增加Logs信息
        span.log(ImmutableMap.of("event", "string-format", "value", helloStr));

        System.out.println(helloStr);
        // 增加Logs信息
        span.log(ImmutableMap.of("event", "println"));

        span.finish();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expecting one argument");
        }
        String helloTo = args[0];

        Tracer tracer = initTracer("hello-world");
        new Hello(tracer).sayHello(helloTo);
    }

    public static JaegerTracer initTracer(String service) {
        SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv().withType("const").withParam(1);
        ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv().withLogSpans(true);
        Configuration config = new Configuration(service).withSampler(samplerConfig).withReporter(reporterConfig);
        return config.getTracer();
    }
}