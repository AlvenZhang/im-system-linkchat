package com.linkchat.netty.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkchat.netty.model.WebSocketMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * WebSocket消息解码器
 * 将TextWebSocketFrame解码为WebSocketMessage对象
 */
public class MessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        // 从TextWebSocketFrame中获取JSON字符串
        String json = msg.text();
        // 将JSON字符串转换为WebSocketMessage对象
        WebSocketMessage message = objectMapper.readValue(json, WebSocketMessage.class);
        // 添加到输出列表
        out.add(message);
    }
}