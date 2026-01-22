package com.linkchat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Netty服务器启动类
 */
@Slf4j
@Service
public class NettyServer implements InitializingBean, DisposableBean {
    
    /**
     * Netty服务器端口
     */
    @Value("${netty.port:8081}")
    private int port;
    
    /**
     * Boss线程组：处理连接请求
     */
    private EventLoopGroup bossGroup;
    
    /**
     * Worker线程组：处理连接的I/O操作
     */
    private EventLoopGroup workerGroup;
    
    /**
     * 启动Netty服务器
     */
    public void start() {
        try {
            // 初始化线程组
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            
            // 创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 使用NIO通道
                    .option(ChannelOption.SO_BACKLOG, 128) // 连接队列大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 保持连接
                    .childHandler(new WebSocketChannelInitializer()); // 初始化Channel
            
            // 绑定端口，启动服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("Netty WebSocket服务器已启动，端口：{}", port);
            
            // 等待服务器关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Netty服务器启动失败：{}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } finally {
            // 优雅关闭线程组
            stop();
        }
    }
    
    /**
     * 停止Netty服务器
     */
    public void stop() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        log.info("Netty WebSocket服务器已停止");
    }
    
    /**
     * Spring容器初始化时调用，启动Netty服务器
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 启动Netty服务器（异步）
        new Thread(this::start, "netty-server").start();
    }
    
    /**
     * Spring容器销毁时调用，停止Netty服务器
     */
    @Override
    public void destroy() throws Exception {
        stop();
    }
}