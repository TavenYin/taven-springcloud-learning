package com.github.taven.tracing.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.github.taven.tracing.log.SpanLogsAppender;
import io.opentracing.Tracer;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 启用该配置后，如果当前上下文中存在 Span，期间 Logger 内容都会被添加到 Span 的 log 属性中
 */
@Configuration
@ConditionalOnProperty(name = "opentracing.logback-support.enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {

    public LoggingAutoConfiguration(Tracer tracer) {
        SpanLogsAppender spanLogsAppender = new SpanLogsAppender(tracer);
        spanLogsAppender.start();
        Logger rootLogger = getRootLogger();
        rootLogger.addAppender(spanLogsAppender);
    }

    private Logger getRootLogger() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    }
}
