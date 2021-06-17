### 启动控制台

```
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar
```

### 客户端接入控制台

启动参数添加 
```
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080
```

### 集群限流

#### 内嵌模式

1. 启动 SentinelClusterEmbedded，添加如下参数
```
-Dproject.name=appA -Dcsp.sentinel.dashboard.server=127.0.0.1:8080
```

2. 去控制台 -> 集群流控 -> 新增 Token Server
选择Server 以及Client 提交
   
#### 小记
默认情况下每个Sentinel 节点启动后 状态为 `—1` （既不是client 也不是Server）
即使规则配置的是集群限流，由于没有指定节点身份，以及对应的server。
此时集群限流不会生效，如果有本地限流策略，则使用本地限流策略，本地没有策略则不限流

所以需要在控制台手动操作一下。

但是为什么Sentinel不做一个像其他应用那样的自动选主功能？
