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

1. 将 jaeger-mutilserver 导入 IDEA

2. 编译 taven-spring-boot-starter-jaeger

```shell
cd taven-spring-boot-starter-jaeger
mvn clean install
```
或者 直接在 IDE maven 菜单中添加该工程（这种方式更方便调试）

3. 链路 TraceId 可以在 Http ResponseHeader 中看到
