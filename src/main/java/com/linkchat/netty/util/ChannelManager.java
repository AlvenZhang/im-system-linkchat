package com.linkchat.netty.util;

import com.linkchat.netty.model.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket连接管理器
 * 管理在线用户的WebSocket连接
 */
public class ChannelManager {
    
    /**
     * Channel属性键：用户ID
     */
    public static final AttributeKey<String> USER_ID = AttributeKey.newInstance("userId");
    
    /**
     * 用户ID与Channel的映射
     */
    private static final Map<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();
    
    /**
     * ChannelId与用户ID的映射
     */
    private static final Map<ChannelId, String> CHANNEL_USER_MAP = new ConcurrentHashMap<>();
    
    /**
     * 添加Channel到管理器
     * @param userId 用户ID
     * @param channel Channel
     */
    public static void addChannel(String userId, Channel channel) {
        // 将用户ID设置到Channel属性中
        channel.attr(USER_ID).set(userId);
        // 添加映射关系
        USER_CHANNEL_MAP.put(userId, channel);
        CHANNEL_USER_MAP.put(channel.id(), userId);
    }
    
    /**
     * 从管理器中移除Channel
     * @param channel Channel
     */
    public static void removeChannel(Channel channel) {
        String userId = channel.attr(USER_ID).get();
        if (userId != null) {
            USER_CHANNEL_MAP.remove(userId);
        }
        CHANNEL_USER_MAP.remove(channel.id());
    }
    
    /**
     * 根据用户ID获取Channel
     * @param userId 用户ID
     * @return Channel，如果用户不在线则返回null
     */
    public static Channel getChannel(String userId) {
        return USER_CHANNEL_MAP.get(userId);
    }
    
    /**
     * 根据Channel获取用户ID
     * @param channel Channel
     * @return 用户ID，如果Channel未关联用户则返回null
     */
    public static String getUserId(Channel channel) {
        return channel.attr(USER_ID).get();
    }
    
    /**
     * 检查用户是否在线
     * @param userId 用户ID
     * @return true表示在线，false表示离线
     */
    public static boolean isOnline(String userId) {
        return USER_CHANNEL_MAP.containsKey(userId);
    }
    
    /**
     * 获取在线用户数量
     * @return 在线用户数量
     */
    public static int getOnlineUserCount() {
        return USER_CHANNEL_MAP.size();
    }
    
    /**
     * 向指定用户发送消息
     * @param userId 用户ID
     * @param message 消息
     * @return true表示发送成功，false表示用户不在线
     */
    public static boolean sendMessage(String userId, WebSocketMessage message) {
        Channel channel = USER_CHANNEL_MAP.get(userId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
            return true;
        }
        return false;
    }
    
    /**
     * 向指定Channel发送消息
     * @param channel Channel
     * @param message 消息
     */
    public static void sendMessage(Channel channel, WebSocketMessage message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }
    
    /**
     * 向所有在线用户广播消息
     * @param message 消息
     */
    public static void broadcastMessage(WebSocketMessage message) {
        for (Channel channel : USER_CHANNEL_MAP.values()) {
            if (channel.isActive()) {
                channel.writeAndFlush(message);
            }
        }
    }
    
    /**
     * 向除了指定用户之外的所有在线用户广播消息
     * @param exceptUserId 排除的用户ID
     * @param message 消息
     */
    public static void broadcastMessageExcept(String exceptUserId, WebSocketMessage message) {
        for (Map.Entry<String, Channel> entry : USER_CHANNEL_MAP.entrySet()) {
            String userId = entry.getKey();
            Channel channel = entry.getValue();
            if (!userId.equals(exceptUserId) && channel.isActive()) {
                channel.writeAndFlush(message);
            }
        }
    }
}