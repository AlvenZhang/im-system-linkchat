package com.linkchat.netty;

import com.linkchat.netty.codec.MessageDecoder;
import com.linkchat.netty.codec.MessageEncoder;
import com.linkchat.netty.model.WebSocketMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Netty服务器集成测试类
 */
public class NettyServerTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    void testMessageCodec() throws Exception {
        // 创建嵌入通道，添加编解码器
        EmbeddedChannel channel = new EmbeddedChannel(
                new MessageEncoder(),
                new MessageDecoder()
        );
        
        // 创建测试消息
        WebSocketMessage originalMessage = WebSocketMessage.textMessage(
                "user1", "user2", "Hello from test", false
        );
        
        // 测试编码：WebSocketMessage -> TextWebSocketFrame
        channel.writeOutbound(originalMessage);
        TextWebSocketFrame frame = channel.readOutbound();
        assertNotNull(frame);
        
        // 解析JSON字符串，验证编码正确
        WebSocketMessage encodedMessage = objectMapper.readValue(frame.text(), WebSocketMessage.class);
        assertEquals(originalMessage.getType(), encodedMessage.getType());
        assertEquals(originalMessage.getFrom(), encodedMessage.getFrom());
        assertEquals(originalMessage.getTo(), encodedMessage.getTo());
        assertEquals(originalMessage.getContent(), encodedMessage.getContent());
        assertEquals(originalMessage.isGroup(), encodedMessage.isGroup());
        
        // 测试解码：TextWebSocketFrame -> WebSocketMessage
        channel.writeInbound(frame);
        WebSocketMessage decodedMessage = channel.readInbound();
        assertNotNull(decodedMessage);
        
        // 验证解码正确
        assertEquals(originalMessage.getType(), decodedMessage.getType());
        assertEquals(originalMessage.getFrom(), decodedMessage.getFrom());
        assertEquals(originalMessage.getTo(), decodedMessage.getTo());
        assertEquals(originalMessage.getContent(), decodedMessage.getContent());
        assertEquals(originalMessage.isGroup(), decodedMessage.isGroup());
        
        // 关闭通道
        channel.close();
    }
    
    @Test
    void testHeartbeatMessage() throws Exception {
        // 创建嵌入通道，添加完整的处理器链
        EmbeddedChannel channel = new EmbeddedChannel(
                new MessageEncoder(),
                new MessageDecoder(),
                new NettyServerHandler()
        );
        
        // 创建心跳消息
        WebSocketMessage heartbeat = WebSocketMessage.heartbeat();
        
        // 发送心跳消息
        channel.writeInbound(heartbeat);
        
        // 读取响应 - 出站消息经过MessageEncoder编码后会变成TextWebSocketFrame
        TextWebSocketFrame responseFrame = channel.readOutbound();
        assertNotNull(responseFrame);
        
        // 解析响应内容
        WebSocketMessage response = objectMapper.readValue(responseFrame.text(), WebSocketMessage.class);
        assertNotNull(response);
        assertEquals(WebSocketMessage.HEARTBEAT, response.getType());
        
        // 关闭通道
        channel.close();
    }
    
    @Test
    void testTextMessageHandling() {
        // 创建嵌入通道，添加完整的处理器链
        EmbeddedChannel channel = new EmbeddedChannel(
                new MessageEncoder(),
                new MessageDecoder(),
                new NettyServerHandler()
        );
        
        // 创建文本消息
        WebSocketMessage textMessage = WebSocketMessage.textMessage(
                "user1", "user2", "Hello, this is a test message", false
        );
        
        // 发送文本消息
        channel.writeInbound(textMessage);
        
        // 验证消息被正确处理（NettyServerHandler会记录日志）
        // 由于我们使用的是嵌入式通道，不会实际发送到网络，所以这里主要测试处理器是否能正确接收和处理消息
        
        // 关闭通道
        channel.close();
    }
    
    @Test
    void testChannelActiveAndInactive() {
        // 创建嵌入通道，添加NettyServerHandler
        EmbeddedChannel channel = new EmbeddedChannel(new NettyServerHandler());
        
        // 模拟通道激活
        channel.pipeline().fireChannelActive();
        
        // 模拟通道关闭
        channel.pipeline().fireChannelInactive();
        
        // 关闭通道
        channel.close();
    }
}