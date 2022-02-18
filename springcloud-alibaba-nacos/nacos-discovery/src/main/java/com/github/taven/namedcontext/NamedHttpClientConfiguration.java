package com.github.taven.namedcontext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author tianwen.yin
 */
public class NamedHttpClientConfiguration {

    @Value("${http.client.name}")
    private String httpClientName;

    @Bean
    @ConditionalOnMissingBean
    public ClientConfig clientConfig(Environment env) {
        return new ClientConfig(httpClientName, env);
    }

    @Bean
    @ConditionalOnMissingBean
    public NamedHttpClient namedHttpClient(ClientConfig clientConfig) {
        return new NamedHttpClient(httpClientName, clientConfig);
    }

}
