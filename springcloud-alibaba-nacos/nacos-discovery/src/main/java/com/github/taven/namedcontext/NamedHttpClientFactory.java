package com.github.taven.namedcontext;

import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.stereotype.Component;

/**
 * @author tianwen.yin
 */
@Component
public class NamedHttpClientFactory extends NamedContextFactory<NamedHttpClientSpec> {

    public NamedHttpClientFactory() {
        super(NamedHttpClientConfiguration.class, "namedHttpClient", "http.client.name");
    }

}
