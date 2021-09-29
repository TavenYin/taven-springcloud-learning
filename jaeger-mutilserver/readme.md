### 目标

手动实现 openTracing 跨服务上下文传递

流程：浏览器请求 order-server，order-server 请求 product-server

### 功能列表

1. 将 Logger 内容同步到 Span 的 log 属性中

    实现参考：com.github.taven.tracing.web.TracingRestTemplateInterceptor

2. RestTemplate 将 SpanContext 注入到 HttpHeader 中

   实现参考：com.github.taven.tracing.log.SpanLogsAppender

3. Servlet Filter 解析 HttpHeader 中的内容，抽取到 SpanContext，实现跨线程传递

   实现参考：com.github.taven.tracing.web.TracingFilter

### 食用指南

1. 编译 taven-spring-boot-starter-jaeger

```shell
mvn clean install
```

2. 将 jaeger-mutilserver 导入 IDEA