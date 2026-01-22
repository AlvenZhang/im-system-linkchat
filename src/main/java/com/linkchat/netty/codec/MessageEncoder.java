package com.linkchat.netty.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkchat.netty.model.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * WebSocket消息编码器
 * 将WebSocketMessage对象编码为TextWebSocketFrame
 */
public class MessageEncoder extends MessageToMessageEncoder<WebSocketMessage> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void encode(ChannelHandlerContext ctx, WebSocketMessage msg, List<Object> out) throws Exception {
        // 将WebSocketMessage对象转换为JSON字符串
        String json = objectMapper.writeValueAsString(msg);
        // 包装为TextWebSocketFrame
        out.add(new TextWebSocketFrame(json));
    }
}