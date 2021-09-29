package com.github.taven.tracing.config;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.Configuration.SenderConfiguration;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class TracerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Tracer getTracer() {
        SamplerConfiguration samplerConfig =
                SamplerConfiguration.fromEnv()
                        .withType("const")
                        .withParam(1);
        // 修改 sender 配置 jaeger collector 或者 agent 地址
        // 默认会创建 UdpSender
        // 默认 agent 地址 localhost:6831
        SenderConfiguration senderConfiguration =
                SenderConfiguration
                        .fromEnv();
        ReporterConfiguration reporterConfig =
                ReporterConfiguration
                        .fromEnv()
                        .withSender(senderConfiguration)
                        .withLogSpans(true);

        return new Configuration(applicationName)
                .withReporter(reporterConfig)
                .withSampler(samplerConfig)
                .getTracerBuilder()
                .build();
    }

}
