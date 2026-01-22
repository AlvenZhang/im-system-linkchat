package com.linkchat.infrastructure.netty;

import com.linkchat.infrastructure.netty.codec.MessageDecoder;
import com.linkchat.infrastructure.netty.codec.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * WebSocket Channel初始化器
 * 初始化Channel的Handler链
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    
    /**
     * WebSocket路径
     */
    private static final String WEBSOCKET_PATH = "/ws";
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        
        // HTTP相关Handler
        pipeline.addLast(new HttpServerCodec()); // HTTP编解码器
        pipeline.addLast(new ChunkedWriteHandler()); // 处理大文件传输
        pipeline.addLast(new HttpObjectAggregator(65536)); // HTTP消息聚合
        
        // WebSocket协议升级Handler
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true, 65536));
        
        // 空闲状态检测Handler
        pipeline.addLast(new IdleStateHandler(30, 30, 60));
        
        // 自定义消息编解码器
        pipeline.addLast(new MessageEncoder()); // 消息编码器
        pipeline.addLast(new MessageDecoder()); // 消息解码器
        
        // 自定义消息处理器
        pipeline.addLast(new NettyServerHandler()); // WebSocket消息处理器
    }
}