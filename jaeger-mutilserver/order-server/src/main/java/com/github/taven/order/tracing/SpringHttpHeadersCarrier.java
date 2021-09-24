package com.github.taven.order.tracing;

import io.opentracing.propagation.TextMap;
import org.springframework.http.HttpHeaders;

import java.util.Iterator;
import java.util.Map;

public class SpringHttpHeadersCarrier implements TextMap {
    private final HttpHeaders httpHeaders;

    public SpringHttpHeadersCarrier(HttpHeaders httpHeaders)  {
        this.httpHeaders = httpHeaders;
    }

    @Override
    public void put(String key, String value) {
        httpHeaders.add(key, value);
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        throw new UnsupportedOperationException("Should be used only with tracer#inject()");
    }
}
