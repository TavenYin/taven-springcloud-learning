package com.github.taven.tracing.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import io.opentracing.Tracer;

public class SpanLogsAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private final Tracer tracer;

    public SpanLogsAppender(Tracer tracer) {
        this.name = SpanLogsAppender.class.getSimpleName();
        this.tracer = tracer;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

    }
}
