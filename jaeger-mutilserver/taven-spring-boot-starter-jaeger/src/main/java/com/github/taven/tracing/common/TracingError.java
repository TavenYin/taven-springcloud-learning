package com.github.taven.tracing.common;

import io.opentracing.Span;
import io.opentracing.tag.Tags;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianwen.yin
 */
public class TracingError {

    public static void handle(Span span, Throwable ex) {
        Tags.ERROR.set(span, Boolean.TRUE);
        Map<String, Object> errorLogs = new HashMap<>(2);
        errorLogs.put("event", Tags.ERROR.getKey());
        errorLogs.put("error.object", ex);
        span.log(errorLogs);
    }

}
