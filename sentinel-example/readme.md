启动控制台

```
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar
```

客户端接入控制台

启动参数添加 
```
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080
```