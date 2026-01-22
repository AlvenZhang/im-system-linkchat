package com.linkchat.netty.util;

import com.linkchat.netty.model.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ChannelManager测试类
 */
public class ChannelManagerTest {
    
    @Test
    void testAddAndRemoveChannel() {
        // 创建嵌入通道用于测试
        EmbeddedChannel channel1 = new EmbeddedChannel();
        EmbeddedChannel channel2 = new EmbeddedChannel();
        
        // 添加Channel
        ChannelManager.addChannel("user1", channel1);
        ChannelManager.addChannel("user2", channel2);
        
        // 验证添加成功
        assertEquals(2, ChannelManager.getOnlineUserCount());
        assertEquals(channel1, ChannelManager.getChannel("user1"));
        assertEquals(channel2, ChannelManager.getChannel("user2"));
        assertEquals("user1", ChannelManager.getUserId(channel1));
        assertEquals("user2", ChannelManager.getUserId(channel2));
        assertTrue(ChannelManager.isOnline("user1"));
        assertTrue(ChannelManager.isOnline("user2"));
        
        // 移除Channel
        ChannelManager.removeChannel(channel1);
        
        // 验证移除成功
        assertEquals(1, ChannelManager.getOnlineUserCount());
        assertNull(ChannelManager.getChannel("user1"));
        assertFalse(ChannelManager.isOnline("user1"));
        assertTrue(ChannelManager.isOnline("user2"));
        
        // 关闭通道
        channel1.close();
        channel2.close();
    }
    
    @Test
    void testSendMessage() {
        // 创建嵌入通道用于测试
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelManager.addChannel("user1", channel);
        
        // 创建测试消息
        WebSocketMessage message = WebSocketMessage.textMessage("sender", "user1", "Hello", false);
        
        // 发送消息
        boolean result = ChannelManager.sendMessage("user1", message);
        assertTrue(result);
        
        // 发送给离线用户
        boolean offlineResult = ChannelManager.sendMessage("offlineUser", message);
        assertFalse(offlineResult);
        
        // 关闭通道
        channel.close();
    }
    
    @Test
    void testBroadcastMessage() {
        // 创建嵌入通道用于测试
        EmbeddedChannel channel1 = new EmbeddedChannel();
        EmbeddedChannel channel2 = new EmbeddedChannel();
        
        ChannelManager.addChannel("user1", channel1);
        ChannelManager.addChannel("user2", channel2);
        
        // 创建测试消息
        WebSocketMessage message = WebSocketMessage.textMessage("sender", "broadcast", "Hello everyone", false);
        
        // 广播消息
        ChannelManager.broadcastMessage(message);
        
        // 广播消息给除了指定用户之外的所有用户
        ChannelManager.broadcastMessageExcept("user1", message);
        
        // 关闭通道
        channel1.close();
        channel2.close();
    }
    
    @Test
    void testUserOnlineStatus() {
        // 创建嵌入通道用于测试
        EmbeddedChannel channel = new EmbeddedChannel();
        
        // 初始状态：离线
        assertFalse(ChannelManager.isOnline("user1"));
        
        // 添加Channel：在线
        ChannelManager.addChannel("user1", channel);
        assertTrue(ChannelManager.isOnline("user1"));
        
        // 移除Channel：离线
        ChannelManager.removeChannel(channel);
        assertFalse(ChannelManager.isOnline("user1"));
        
        // 关闭通道
        channel.close();
    }
}