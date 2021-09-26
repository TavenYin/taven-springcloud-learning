package com.github.taven.product.tracing;

import io.opentracing.propagation.TextMap;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author tianwen.yin
 */
public class HttpServletRequestExtractAdapter implements TextMap {
    private final IdentityHashMap<String, String> headers;

    public HttpServletRequestExtractAdapter(HttpServletRequest httpServletRequest) {
        headers = servletHeadersToMap(httpServletRequest);
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }

    @Override
    public void put(String key, String value) {
        throw new UnsupportedOperationException("This class should be used only with Tracer.inject()!");
    }

    private IdentityHashMap<String, String> servletHeadersToMap(HttpServletRequest httpServletRequest) {
        IdentityHashMap<String, String> headersResult = new IdentityHashMap<>();

        Enumeration<String> headerNamesIt = httpServletRequest.getHeaderNames();
        while (headerNamesIt.hasMoreElements()) {
            String headerName = headerNamesIt.nextElement();

            Enumeration<String> valuesIt = httpServletRequest.getHeaders(headerName);
            while (valuesIt.hasMoreElements()) {
                // IdentityHashMap 判断两个 Key 相等的条件为 k1 == k2
                // 为了让两个相同的字符串同时存在，必须使用 new String
                headersResult.put(new String(headerName), valuesIt.nextElement());
            }

        }

        return headersResult;
    }

}
