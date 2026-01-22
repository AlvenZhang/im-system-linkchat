# Netty WebSocket服务器实现计划

## 1. 添加Netty依赖
- 在pom.xml中添加Netty核心依赖
- 版本选择4.1.100（与设计方案一致）

## 2. 实现消息模型
- 创建WebSocket消息实体类
- 定义消息类型常量
- 实现消息的序列化和反序列化

## 3. 实现Netty组件
- **NettyServer**：Netty服务器启动类
- **NettyServerHandler**：处理WebSocket连接和消息
- **WebSocketChannelInitializer**：初始化Channel和Handler
- **MessageEncoder**：消息编码器
- **MessageDecoder**：消息解码器

## 4. 实现WebSocket连接管理
- 管理在线用户的WebSocket连接
- 实现用户ID与Channel的映射
- 实现连接的建立、关闭和心跳检测

## 5. 集成Spring Boot
- 将Netty服务器作为Spring Boot组件启动
- 配置Netty服务器的端口和线程模型
- 实现Netty与Spring容器的集成

## 6. 测试与验证
- 启动Netty服务器
- 测试WebSocket连接建立
- 测试消息发送和接收

## 7. 代码结构设计
```
com.linkchat
├── netty
│   ├── NettyServer.java          # Netty服务器启动类
│   ├── NettyServerHandler.java   # WebSocket消息处理器
│   ├── WebSocketChannelInitializer.java # Channel初始化
│   ├── codec
│   │   ├── MessageEncoder.java   # 消息编码器
│   │   └── MessageDecoder.java   # 消息解码器
│   ├── model
│   │   └── WebSocketMessage.java # WebSocket消息模型
│   └── util
│       └── ChannelManager.java   # 连接管理器
└── config
    └── NettyConfig.java          # Netty配置类
```