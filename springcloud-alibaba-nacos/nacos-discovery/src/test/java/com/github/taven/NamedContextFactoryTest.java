package com.github.taven;

import com.github.taven.namedcontext.NamedHttpClient;
import com.github.taven.namedcontext.NamedHttpClientFactory;
import com.github.taven.namedcontext.NamedHttpClientSpec;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tianwen.yin
 */
public class NamedContextFactoryTest {

    private void initEnv(AnnotationConfigApplicationContext parent) {
        Map<String, Object> map = new HashMap<>();
        map.put("baidu.socketTimeout", 123);
        map.put("google.socketTimeout", 456);
        parent.getEnvironment()
                .getPropertySources()
                .addFirst(new MapPropertySource("test", map));
    }

    @Test
    public void test() {
        // 创建 parent context
        AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
        parent.register(ParentConfiguration.class);
        initEnv(parent);
        parent.refresh();

        // name = baidu 的 context 中会注册 TestConfiguration
        NamedHttpClientSpec spec = new NamedHttpClientSpec("baidu", new Class[]{TestConfiguration.class});

        NamedHttpClientFactory namedHttpClientFactory = new NamedHttpClientFactory();
        // SpringBoot 中无需手动设置，会自动注入 parent
        namedHttpClientFactory.setApplicationContext(parent);
        namedHttpClientFactory.setConfigurations(List.of(spec));

        // 准备工作完成，现在开始通过 NamedContextFactory get Bean
        NamedHttpClient baidu = namedHttpClientFactory.getInstance("baidu", NamedHttpClient.class);
        TestBean baiduTestBean = namedHttpClientFactory.getInstance("baidu", TestBean.class);
        Assert.assertEquals("baidu", baidu.getServiceName());
        Assert.assertEquals(123, baidu.getRequestConfig().getSocketTimeout());
        Assert.assertNotNull(baiduTestBean);

        NamedHttpClient google = namedHttpClientFactory.getInstance("google", NamedHttpClient.class);
        TestBean googleTestBean = namedHttpClientFactory.getInstance("google", TestBean.class);
        Assert.assertEquals("google", google.getServiceName());
        Assert.assertEquals(456, google.getRequestConfig().getSocketTimeout());
        Assert.assertNull(googleTestBean);
    }

    @Configuration
    static class ParentConfiguration {
        @Bean
        public ParentBean parentBean() {
            return new ParentBean();
        }
    }

    @Configuration
    static class TestConfiguration {
        @Bean
        public TestBean testBean() {
            return new TestBean();
        }
    }


    static class ParentBean {

    }

    static class TestBean {

    }

}
